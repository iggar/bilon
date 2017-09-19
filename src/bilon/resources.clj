(ns bilon.resources
  (:require [yada.yada :refer [listener resource as-resource]]
            [hiccup.page :refer [html5]]
            [bilon.services :refer [nearest-from get-all]]
            [bilon.helpers.config :as config]))

(defn- welcome []
  (html5 [:body
        [:h1 "Bilon"]
        [:h2 "List of public bicycle stations in London"]
        [:h3 "Current configuration"]
        [:pre (str (config/all))]]))

(def main-page
  (resource
      {:produces "text/html"
      :response (welcome)}))

(defn bike-table [location]
  (nearest-from location config/num-results (get-all "https://api.tfl.gov.uk/BikePoint/")))

(defn bike-row-html [item]
  (let [distance (:distance item)
        distance-km (format "%.2f" distance)
        distance-mi (format "%.2f" (/ distance 1.6))
        lat (:lat (:lat-lon item))
        lng (:lng (:lat-lon item))
        url "https://www.google.co.uk/maps/search/?api=1&query="
        link (str url lat "," lng)]
    [:tr
      [:td [:a {:href link} (:name item)]]
      [:td {:style "text-align: center;"} (:bikes-free item)]
      [:td (str distance-km "km (" distance-mi "mi)")]]))

(defn- bike-list-json []
   (bike-table config/base-coordinates))

(defn bike-table-html []
  (let [items (bike-list-json)]
        name (:name items)
    (html5 [:table
              [:thead
                [:tr
                  [:th "Bike Point"]
                  [:th "# Bikes Available"]
                  [:th "Distance"]]]
              [:tbody
                (map bike-row-html items)]])))

(defn- bike-list-page []
    (html5 [:body
              [:h1 "Welcome to BiLon"]
              [:h2 "Find a public bicycle near you."]
              [:p "Here is a list of bicycle stations around you right now. "
                  "Click the link to open the location on Google Maps."]
              (bike-table-html)]))

(def bike-list
  (resource
   {:id :bilon-resource/bike-list
    :methods
    {:get {:produces "text/html"
           :response (bike-list-page)}}}))

(def api-bike-list
   (resource
    {:id :bilon-resource/bike-list-json
     :methods
     {:get {:produces "application/json"
            :response (bike-list-json)}}

     :access-control
     {:scheme "Basic"
      :verify (fn [[user password]]
                (when (and (= user "juxt") (= password "txuj"))
                  {:user user
                   :roles #{:user}}))
      :authorization {:methods {:get :user}}}}))

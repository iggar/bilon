(ns bilon.resources
  (:require [yada.yada :refer [listener resource as-resource]]
            [hiccup.page :refer [html5]]
            [bilon.services :refer [nearest-from get-all]]))


(def ^:const leyton {:latitude 51.561948 :longitude -0.013139})
(def ^:const num-entries 5)

(defn- welcome []
  (html5 [:body
        [:h1 "Bilon"]
        [:h2 "List of public bicycle stations in London"]]))

(def main-page
  (resource
      {:produces "text/html"
      :response welcome}))

(defn bike-table [location]
  (nearest-from location num-entries (get-all "https://api.tfl.gov.uk/BikePoint/")))

(defn- bike-list-page [ctx]
    (html5 [:body
    [:h1 (format "Hello %s!" (get-in ctx [:authentication "default" :user]))]
    [:p "Here is a list of bicycle stations around you right now:"]
    [:pre (bike-table leyton)]]))

(def bike-list
  (resource
   {:id :bilon-resource/bike-list
    :methods
    {:get {:produces "text/html"
           :response (fn [ctx] (bike-list-page ctx))}}

    :access-control
    {:scheme "Basic"
     :verify (fn [[user password]]
               (when (and (= user "juxt") (= password "txuj"))
                 {:user user
                  :roles #{:user}}))
     :authorization {:methods {:get :user}}}}))

(defn- bike-list-json []
   (bike-table leyton))

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

(ns bilon.resources
  (:require [yada.yada :refer [listener resource as-resource]]
            [hiccup.page :refer [html5]]
            [bilon.services :refer [get-feed]]))

(defn- welcome []
  (html5 [:body
        [:h1 "Bilon"]
        [:h2 "List of public bicycle stations in London"]]))

(def main-page
  (resource
      {:produces "text/html"
      :response welcome}))

(defn bike-table []
  (let [top5 (get-feed "https://api.tfl.gov.uk/BikePoint/" 5)]
      (apply str (map #(str "name: " (:commonName %) "\n") top5))))


(defn- bike-list-page [ctx]
    (html5 [:body
    [:h1 (format "Hello %s!" (get-in ctx [:authentication "default" :user]))]
    [:p "Here is a list of bicycle stations around you right now:"]
    [:pre (bike-table)]]))

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

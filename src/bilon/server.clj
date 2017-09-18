(ns bilon.server
  (:require [yada.yada :refer [listener resource as-resource]]
            [hiccup.page :refer [html5]])
  (:gen-class))

(defn- welcome []
  (html5 [:body
        [:h1 "Bilon"]
        [:h2 "List of public bicycle stations in London"]]))

(def main-page
  (resource
      {:produces "text/html"
      :response welcome}))

(defn- bike-list-page [ctx]
    (html5 [:body
    [:h1 (format "Hello %s!" (get-in ctx [:authentication "default" :user]))]
    [:p "Here is a list of bicycle stations around you right now:"]
    [:pre (pr-str (get-in ctx [:authentication "default"]))]]))

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


(defn -main
 [& args]
 (let [listen (listener
                ["/"
                 [
                  ["hello" (as-resource "Hello World!")]
                  ["test" (resource {:produces "text/plain"
                                     :response "This is a test!"})]
                  ["bikelist" bike-list]
                  ["main" main-page]
                  [true (as-resource nil)]]]
                {:port 3000})]
 (println "Server starting...")
  @(promise))) ;prevents the server from dying

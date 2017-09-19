(ns bilon.server
  (:require [yada.yada :refer [listener resource as-resource]]
            [bilon.resources :refer [bike-list api-bike-list main-page]]
            [bilon.helpers.config :as config])
  (:gen-class))

(defn -main
 [& args]
 (let [listen (listener
                ["/"
                 [
                  ["hello" (as-resource "Hello World!")]
                  ["bikelist" bike-list]
                  ["main" main-page]
                  ["api/bikelist" api-bike-list]
                  ["healthcheck" (resource {:produces "text/plain"
                                            :response "This is fine!"})]
                  [true (as-resource nil)]]]
                {:port config/port})]
 (println "Server starting...")
  @(promise))) ;prevents the server from dying

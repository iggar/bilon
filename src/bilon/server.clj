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
                  ["main" main-page]
                  ["bikelist" bike-list]
                  ["api/bikelist" api-bike-list]
                  ["healthcheck" (resource {:produces "text/plain"
                                            :response "This is fine!"})]
                  [true (as-resource nil)]]]
                {:port config/port})]
 (println "Server starting at port " config/port "...")
  @(promise))) ;prevents the server from dying

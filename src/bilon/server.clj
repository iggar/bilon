(ns bilon.server
  (:require [yada.yada :refer [listener resource as-resource]])
  (:gen-class))

(defn -main
 [& args]
 (let [listen (listener
                ["/"
                 [
                  ["hello" (as-resource "Hello World!")]
                  ["test" (resource {:produces "text/plain"
                                     :response "This is a test!"})]
                  [true (as-resource nil)]]]
                {:port 3000})]
 (println "Server starting...")
  @(promise))) ;prevents the server from dying

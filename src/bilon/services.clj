(ns bilon.services
  (:require [clojure.data.json :as json]
            [clj-http.client :as client]))

(defn get-feed [url n]
  (let [feed (json/read-str
                (:body (client/get url {:accept :json})) :key-fn keyword)]
      (take n feed)))

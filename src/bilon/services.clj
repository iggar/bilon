(ns bilon.services
  (:require [clojure.data.json :as json]
            [clj-http.client :as client]))

(defn get-feed [url n]
  (let [feed (json/read-str
                (:body (client/get url {:accept :json})) :key-fn keyword)]
      (take n feed)))

(defn bikes-available [bikepoint]
  (let [additional-props (:additionalProperties bikepoint)]
    (first (remove nil? (map #(if (= "NbBikes" (:key %)) (:value %)) additional-props)))))

(defn point-name [bikepoint]
  (:commonName bikepoint))

(defn point-lat-lon [bikepoint]
  (vector (:lat bikepoint) (:lon bikepoint)))

(defn point->simplepoint [bikepoint]
  {:name (point-name bikepoint)
   :bikes-free (bikes-available bikepoint)
   :lat-lon (point-lat-lon bikepoint)})

(defn simple-list [bikepoints]
  (map point->simplepoint bikepoints))

;(def adprop (:additionalProperties feed))
;(remove nil? (map #(if (= "NbBikes" (:key %)) (:value %)) adprop))

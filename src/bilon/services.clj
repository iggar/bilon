(ns bilon.services
  (:require [clojure.data.json :as json]
            [clj-http.client :as client]
            [haversine.core :as haversine]))

(defn get-all [url]
  (json/read-str
      (:body (client/get url {:accept :json})) :key-fn keyword))

(defn get-feed [url n]
  (let [feed (get-all url)]
      (take n feed)))

(defn bikes-available [bikepoint]
  (let [additional-props (:additionalProperties bikepoint)]
    (first (remove nil? (map #(if (= "NbBikes" (:key %)) (:value %)) additional-props)))))

(defn point-name [bikepoint]
  (:commonName bikepoint))

(defn point-lat-lon [bikepoint]
  {:lat (:lat bikepoint) :lng (:lon bikepoint)})

(defn point->simplepoint [bikepoint]
  {:name (point-name bikepoint)
   :bikes-free (bikes-available bikepoint)
   :lat-lon (point-lat-lon bikepoint)})

(defn simple-list [bikepoints]
  (map point->simplepoint bikepoints))

(defn point-with-distance [bikepoint]
  (assoc-in bikepoint [:distance]
    (haversine/haversine {:latitude 51.561948 :longitude -0.013139}
                         {:latitude (:lat (:lat-lon bikepoint))
                          :longitude (:lng (:lat-lon bikepoint))})))

(defn sorted-list-with-distances [bikepoints]
  (sort-by :distance (map point-with-distance bikepoints)))

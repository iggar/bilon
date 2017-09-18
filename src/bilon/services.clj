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
  {:lat (:lat bikepoint) :lng (:lon bikepoint)})

(defn point->simplepoint [bikepoint]
  {:name (point-name bikepoint)
   :bikes-free (bikes-available bikepoint)
   :lat-lon (point-lat-lon bikepoint)})

(defn simple-list [bikepoints]
  (map point->simplepoint bikepoints))

; Haversine formula
; a = sin²(Δφ/2) + cos φ1 ⋅ cos φ2 ⋅ sin²(Δλ/2)
; c = 2 ⋅ atan2( √a, √(1−a) )
; d = R ⋅ c
; where φ is latitude, λ is longitude, R is earth’s radius (mean radius = 6,371km);
; Extracted originally from: https://gist.github.com/shayanjm/39418c8425c2a66d480f
(defn haversine
  "Implementation of Haversine formula. Takes two sets of latitude/longitude pairs and returns the shortest great circle distance between them (in km)"
  [{lon1 :lng lat1 :lat} {lon2 :lng lat2 :lat}]
  (let [R 6378.137 ; Radius of Earth in km
        dlat (Math/toRadians (- lat2 lat1))
        dlon (Math/toRadians (- lon2 lon1))
        lat1 (Math/toRadians lat1)
        lat2 (Math/toRadians lat2)
        a (+ (* (Math/sin (/ dlat 2)) (Math/sin (/ dlat 2))) (* (Math/sin (/ dlon 2)) (Math/sin (/ dlon 2)) (Math/cos lat1) (Math/cos lat2)))]
      (* R 2 (Math/asin (Math/sqrt a)))))

(defn point-with-distance [bikepoint]
  (assoc-in bikepoint [:distance] (haversine {:lat 51.561948 :lng -0.013139} (:lat-lon bikepoint))))

(defn sorted-list-with-distances [bikepoints]
  (sort-by :distance (map point-with-distance bikepoints)))

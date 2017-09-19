(ns bilon.helpers.config
  (:require
    [environ.core :refer [env]]))

(def port (Integer/parseInt (env :port "3000")))

(def tfl-bikepoint-uri
  (env :tfl-bikepoint-uri "https://api.tfl.gov.uk/BikePoint/"))

(def num-results
  (Integer/parseInt (env :num-results "5")))

;Coordinates for Leyton are the default
(def base-coordinates
  (read-string (env :base-coordinates "{:latitude 51.561948 :longitude -0.013139}")))

(defn all
  "List all the config variables used"
  []
  (reduce-kv
    (fn [acc k v]
      (if-not (= 'all k)
        (merge
          acc {k (var-get v)})
        acc))
    {}
    (ns-publics 'bilon.helpers.config)))

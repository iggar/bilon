(ns bilon.services-test
  (:require [clojure.test :refer :all]
            [bilon.services :refer :all]))

(def a-bikepoint
  {:additionalProperties [{:$type "Tfl.Api.Presentation.Entities.AdditionalProperties, Tfl.Api.Presentation.Entities",
  :category "Description", :key "TerminalName", :sourceSystemKey "BikePoints", :value "001023", :modified "2017-09-19T19:41:06.037Z"}
  {:$type "Tfl.Api.Presentation.Entities.AdditionalProperties, Tfl.Api.Presentation.Entities", :category "Description",
   :key "Installed", :sourceSystemKey "BikePoints", :value "true", :modified "2017-09-19T19:41:06.037Z"}
   {:$type "Tfl.Api.Presentation.Entities.AdditionalProperties, Tfl.Api.Presentation.Entities",
   :category "Description", :key "Locked", :sourceSystemKey "BikePoints", :value "false", :modified "2017-09-19T19:41:06.037Z"}
   {:$type "Tfl.Api.Presentation.Entities.AdditionalProperties, Tfl.Api.Presentation.Entities",
  :category "Description", :key "InstallDate", :sourceSystemKey "BikePoints", :value "1278947280000", :modified "2017-09-19T19:41:06.037Z"}
   {:$type "Tfl.Api.Presentation.Entities.AdditionalProperties, Tfl.Api.Presentation.Entities", :category "Description", :key "RemovalDate",
   :sourceSystemKey "BikePoints", :value "", :modified "2017-09-19T19:41:06.037Z"}
   {:$type "Tfl.Api.Presentation.Entities.AdditionalProperties, Tfl.Api.Presentation.Entities",
    :category "Description", :key "Temporary", :sourceSystemKey "BikePoints", :value "false",
     :modified "2017-09-19T19:41:06.037Z"} {:$type "Tfl.Api.Presentation.Entities.AdditionalProperties, Tfl.Api.Presentation.Entities",
      :category "Description", :key "NbBikes", :sourceSystemKey "BikePoints", :value "9", :modified "2017-09-19T19:41:06.037Z"}
      {:$type "Tfl.Api.Presentation.Entities.AdditionalProperties, Tfl.Api.Presentation.Entities",
       :category "Description", :key "NbEmptyDocks", :sourceSystemKey "BikePoints", :value "10",
       :modified "2017-09-19T19:41:06.037Z"} {:$type "Tfl.Api.Presentation.Entities.AdditionalProperties, Tfl.Api.Presentation.Entities",
       :category "Description", :key "NbDocks", :sourceSystemKey "BikePoints", :value "19", :modified "2017-09-19T19:41:06.037Z"}],
       :childrenUrls [], :$type "Tfl.Api.Presentation.Entities.Place, Tfl.Api.Presentation.Entities",
       :children [], :placeType "BikePoint", :id "BikePoints_1",
       :lon -0.10997, :url "/Place/BikePoints_1", :lat 51.529163, :commonName "River Street , Clerkenwell"})

(deftest point-name-test
  (testing "Bike point name"
    (let [name (point-name a-bikepoint)]
    (is (= name "River Street , Clerkenwell")))))

(deftest bikes-available-test
  (testing "Check if number of bikes available is being extracted correctly"
    (let [avail (bikes-available a-bikepoint)]
      (is (= avail "9")))))

(def simplifiedpoint
  {:name "River Street , Clerkenwell", :bikes-free "9", :lat-lon {:lat 51.529163, :lng -0.10997}})

(deftest simple-point
  (testing "Check if the bike point adapter is transforming the record correctly"
    (let [simpoint (point->simplepoint a-bikepoint)]
      (is (= simpoint simplifiedpoint)))))

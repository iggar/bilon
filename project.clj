(defproject bilon "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [yada "1.2.0"]
                 [aleph "0.4.3"]
                 [bidi "2.0.12"]
                 [hiccup "1.0.5"]
                 [org.clojure/data.json "0.2.6"]
                 [clj-http "3.7.0"]
                 [haversine "0.1.1"]]
  :main ^:skip-aot bilon.server
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})

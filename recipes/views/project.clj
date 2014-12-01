(defproject views "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2371"]

                 [racehub/om-bootstrap "0.3.1"]
                 [om "0.8.0-alpha2"]
                 [sablono "0.2.20"]]

  :plugins [[lein-cljsbuild "1.0.4-SNAPSHOT"]]

  :source-paths ["src"]

  :cljsbuild {
    :builds [{:id "views"
              :source-paths ["src"]
              :compiler {
                :output-to "views.js"
                :output-dir "out"
                :optimizations :none
                :source-map true}}]})

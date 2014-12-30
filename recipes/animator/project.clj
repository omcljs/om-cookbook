(defproject animator "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2371"]

               [om "0.8.0-alpha2"]
               [sablono "0.2.20"]
               [org.clojure/core.async "0.1.346.0-17112a-alpha"]]

  :plugins [[lein-cljsbuild "1.0.4-SNAPSHOT"]]

  :source-paths ["src"]

  :cljsbuild {
    :builds [{:id "animator"
              :source-paths ["src"]
              :compiler {
                :output-to "animator.js"
                :output-dir "out"
                :optimizations :none
                :source-map true}}]})

#!/usr/bin/env boot

(set-env!
  :source-paths   #{"src"}
  :resource-paths #{"html"}
  :dependencies '[[adzerk/boot-cljs "0.0-2814-0" :scope "test"]
                  [adzerk/boot-cljs-repl "0.1.9" :scope "test"]
                  [adzerk/boot-reload "0.2.3" :scope "test"]
                  [pandeiro/boot-http "0.6.2" :scope "test"]
                  [org.clojure/clojure "1.7.0-alpha5"]
                  [org.clojure/clojurescript "0.0-2850"]
                  [om "0.8.0-beta1"]])

(task-options!
  pom {:project 'boot-setup
       :version "0.1.0-SNAPSHOT"})

(require '[adzerk.boot-cljs :refer :all]
 '[adzerk.boot-cljs-repl :refer :all]
 '[adzerk.boot-reload :refer :all]
 '[pandeiro.boot-http :refer [serve]])

(deftask dev
  "Development environment"
  []
  (comp (serve :dir "target")
        (watch)
        (cljs-repl)
        (reload :on-jsload 'boot-setup.core/main)
        (cljs :source-map true
              :optimizations :none
              :unified-mode true)))

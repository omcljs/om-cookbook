#!/usr/bin/env boot

(set-env!
  :src-paths #{"src"}
  :rsc-paths #{"html"}
  :dependencies '[[adzerk/boot-cljs "0.0-2371-27" :scope "test"]
                  [adzerk/boot-cljs-repl "0.1.6" :scope "test"]
                  [adzerk/boot-reload "0.1.6" :scope "test"]
                  [org.clojure/clojure "1.7.0-alpha4"]
                  [org.clojure/clojurescript "0.0-2371"]
                  [om "0.8.0-beta1"]])

(task-options!
  pom [:project 'boot-setup
       :version "0.1.0-SNAPSHOT"])

(require '[adzerk.boot-cljs :refer :all]
 '[adzerk.boot-cljs-repl :refer :all]
 '[adzerk.boot-reload :refer :all])

(deftask dev
  "Development environment"
  []
  (comp (watch)
        (cljs-repl)
        (cljs :source-map true
              :optimizations :none
              :unified true)
        (reload :on-jsload 'boot-setup.core/main)))

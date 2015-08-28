#!/usr/bin/env boot

(set-env!
  :source-paths   #{"src"}
  :resource-paths #{"resources"}
  :dependencies '[[org.clojure/clojure "1.7.0"]
                  [org.clojure/clojurescript "0.0-3308"]
                  [adzerk/boot-cljs "0.0-3308-0" :scope "test"]
                  [adzerk/boot-cljs-repl "0.1.10-SNAPSHOT" :scope "test"]
                  [adzerk/boot-reload "0.3.1" :scope "test"]
                  [pandeiro/boot-http "0.6.2" :scope "test"]
                  [org.omcljs/om "0.8.8"]])

(task-options!
  pom {:project 'boot-setup
       :version "0.1.0-SNAPSHOT"})

(require '[adzerk.boot-cljs :refer [cljs]]
         '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
         '[adzerk.boot-reload :refer [reload]]
         '[pandeiro.boot-http :refer [serve]])

(deftask dev
  "Development environment"
  []
  ; Check `boot <task> --help` for more documentation on the task options.
  (comp
    ; Serve the files directly from classpath. The files to serve located
    ; inside public/ prefix.  This way e.g. the source code or other things
    ; in classpath are not available through http.
    (serve :resource-root "public")
    (watch)
    ; Starts a nrepl server with cljs middlewares.
    (cljs-repl)
    ; The interesting files in fileset/classpath and inside public prefix.
    ; The asset-path option removes given path from urls being reloaded,
    ; so when "public/index.html" changes, browser is told to reload
    ; "index.html".
    (reload :asset-path "public")
    ; Compiles the cljs. This searches for *.cljs.edn files and finds
    ; src/public/main.cljs.edn, from the relative path of the file inside
    ; fileset (public/main.cljs.edn) it sets the output-to and output-path
    ; for cljs compiler, and thus the resulting js is written to
    ; "public/main.js" and the rest of files to "public/out/".
    ; To make main.js refer to other files without public/ prefix, asset-path
    ; option is set manually.
    (cljs :source-map true :compiler-options {:asset-path "out"})))

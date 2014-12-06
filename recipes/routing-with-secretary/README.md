# Single Page Routing With Secretary

1) Create a new Om project using Chestnut

```bash
lein new chestnut routing-with-secretary -- --om-tools --http-kit
```

2) Use your editor of choice to open the file `routing-with-secretary/src/cljs/core.cljs`

3) Add Secretary to your project file

```clojure
[secretary "1.2.1"]
```

4) Add Secretary to your project requirements

```clojure
(ns routing-with-secretary.core
  (:require [om.core :as om :include-macros true]
            [om-tools.dom :as dom :include-macros true]
            [om-tools.core :refer-macros [defcomponent]]
            [secretary.core :as sec
             :include-macros true]))
```

5) Add Google Closure History related requirements and imports.

```clojure
(ns routing-with-secretary.core
  (:require [om.core :as om :include-macros true]
            [om-tools.dom :as dom :include-macros true]
            [om-tools.core :refer-macros [defcomponent]]
            [secretary.core :as sec
             :include-macros true]
            [goog.events :as events]
            [goog.history.EventType :as EventType])
  (:import goog.History))
```
6) Configure Secretary to have a fallback for browsers that don't support HTML5 history.

```clojure
(sec/set-config! :prefix "#")
```

7) Enable HTML5 history support.

```clojure
(let [history (History.)
      navigation EventType/NAVIGATE]
  (goog.events/listen history
                     navigation
                     #(-> % .-token sec/dispatch!))
  (doto history (.setEnabled true)))
```

8) Create a navigation component.

```clojure
(defcomponent navigation-view [_ _]
  (render
   [_]
   (let [style {:style {:margin "10px;"}}]
     (dom/div style
              (dom/a (assoc style :href "#/") 
                     "Home")
              (dom/a (assoc style :href "#/something") 
                     "Something")
              (dom/a (assoc style :href "#/about") 
                     "About")))))
```

9) Create a view component and a Secretary route for the index page.

```clojure
(defcomponent index-page-view [_ _]
  (render
   [_]
   (dom/div
    (om/build navigation-view {})
    (dom/h1 "Index Page"))))
          
(sec/defroute index-page "/" []
  (om/root index-page-view
           {}
           {:target (. js/document (getElementById "app"))}))
```

10) Create a view component and a Secretary route for the something page.

```clojure
(defcomponent something-page-view [_ _]
  (render
   [_]
   (dom/div
    (om/build navigation-view {})
    (dom/h1 "Something Page"))))
          
(sec/defroute something-page "/something" []
  (om/root something-page-view
           {}
           {:target (. js/document (getElementById "app"))}))
```
11) Create a view component and a Secretary route for the about page.

```clojure
(defcomponent about-page-view [_ _]
  (render
   [_]
   (dom/div
    (om/build navigation-view {})
    (dom/h1 "About Page"))))
          
(sec/defroute about-page "/about" []
  (om/root about-page-view
           {}
           {:target (. js/document (getElementById "app"))}))
```

12) Alter or replace your main function to display the index page view when the page is loaded.

```clojure
(defn main []
  (-> js/document
      .-location
      (set! "#/")))
```
13) Start a REPL with `lein repl`

```
nREPL server started on port 54879 on host 127.0.0.1 - nrepl://127.0.0.1:54879
REPL-y 0.3.5, nREPL 0.2.6
Clojure 1.6.0
Java HotSpot(TM) 64-Bit Server VM 1.8.0_05-b13
Docs: (doc function-name-here)
(find-doc "part-of-name-here")
Source: (source function-name-here)
Javadoc: (javadoc java-object-or-class-here)
Exit: Control+D or (exit) or (quit)
Results: Stored in vars *1, *2, *3, an exception in *e
```

14) Call `run` to start the back end and compile your ClojureScript.

```
routing-with-secretary.server=> (run)
Starting figwheel.
Starting web server on port 10555 .
#<clojure.lang.AFunction$1@336fc74>
routing-with-secretary.server=> Compiling ClojureScript.
Figwheel: Starting server at http://localhost:3449
Figwheel: Serving files from '(dev-resources|resources)/public'
Compiling "resources/public/js/app.js" from ("src/cljs" "env/dev/cljs")...
Successfully compiled "resources/public/js/app.js" in 18.01 seconds.
notifying browser that file changed:  /js/out/local_state/core.js
```

15) Point your browser to http://localhost:port. You can find the port in the REPL message output =>  `Starting web server on port ...`

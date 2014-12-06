# Om Toggle Button

1) Create a new Om project using Chestnut

```bash
lein new chestnut toggle-button -- --om-tools --http-kit
```

2) Use your editor of choice to open the file `toggle-button/src/cljs/core.cljs`


3) Your initial app-state should look like this.

```clojure
(defonce app-state (atom {:toggle true}))
```

4) Create a toggle button component

```clojure
(defcomponent toggle-button-view [cursor _]
  (render
   [_]
   (let [on? (:toggle cursor)]
     (dom/div
      {:class "can-toggle"}
      (dom/span "ON")
      (dom/a
       {:href "javascript:void(0)"}
       (dom/span
        {:class "can-toggle-switch"}))
      (dom/span "OFF")))))
```

5) Add button view styles

```clojure
(defcomponent toggle-button-view [cursor _]
  (render
   [_]
   (let [on? (:toggle cursor)
                  [on off] (if on?
                    ["inline" "none"]
                    ["none" "inline"])
         padding "2px 20px 2px 20px;"
         on-attr {:background-color "hsl(100,90%,60%)"
                  :color "hsl(0,90%,60%)"
                  :font-weight "bold"
                  :font-size "20px"
                  :padding padding
                  :display on}
         off-attr {:background-color "hsl(0,90%,60%)"
                   :color "hsl(110,50%,50%)"
                   :font-weight "bold"
                   :font-size "20px"
                   :padding padding
                   :display off}
         toggle-attr {:width "5px;"
                      :height "5px;"
                      :padding "5px;"
                      :border-width "1px;"
                      :border-style "solid;"
                      :border-color "black"}]
     (dom/div
      {:class "can-toggle"
       :style {:width "55px;"}}
      (dom/span {:style on-attr} "ON")
      (dom/a
       {:href "javascript:void(0)"}
       (dom/span
        {:class "can-toggle-switch"
         :style toggle-attr}))
      (dom/span {:style off-attr} "OFF")))))
```

6) Add click logic to toggle using `om/update!`

```clojure
(defcomponent toggle-button-view [cursor _]
  (render
   [_]
   (let [on? (:toggle cursor)
         [on off] (if on?
                    ["inline" "none"]
                    ["none" "inline"])
         padding "2px 20px 2px 20px;"
         on-attr {:background-color "hsl(100,90%,60%)"
                  :color "hsl(0,90%,60%)"
                  :font-weight "bold"
                  :font-size "20px"
                  :padding padding
                  :display on}
         off-attr {:background-color "hsl(0,90%,60%)"
                   :color "hsl(110,50%,50%)"
                   :font-weight "bold"
                   :font-size "20px"
                   :padding padding
                   :display off}
         toggle-attr {:width "5px;"
                      :height "5px;"
                      :padding "5px;"
                      :border-width "1px;"
                      :border-style "solid;"
                      :border-color "black"}]
     (dom/div
      {:class "can-toggle"
       :style {:width "55px;"}}
      (dom/span {:style on-attr} "ON")
      (dom/a
       {:href "javascript:void(0)"
        :on-click (fn [_]
                    (om/update! cursor
                                [:toggle]
                                (not on?)))}
       (dom/span
        {:class "can-toggle-switch"
         :style toggle-attr}))
      (dom/span {:style off-attr} "OFF")))))
```
7) Replace or alter your main function to display the validation box view.

```clojure
(defn main []
  (om/root
   toggle-button-view
   app-state
   {:target (. js/document (getElementById "app"))}))
```
8) Start a REPL with `lein repl`

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

9) Call `run` to start the back end and compile your ClojureScript.

```
toggle-button.server=> (run)
Starting figwheel.
Starting web server on port 10555 .
#<clojure.lang.AFunction$1@336fc74>
toggle-button.server=> Compiling ClojureScript.
Figwheel: Starting server at http://localhost:3449
Figwheel: Serving files from '(dev-resources|resources)/public'
Compiling "resources/public/js/app.js" from ("src/cljs" "env/dev/cljs")...
Successfully compiled "resources/public/js/app.js" in 18.01 seconds.
notifying browser that file changed:  /js/out/local_state/core.js
```

10) Point your browser to http://localhost:port. You can find the port in the REPL message output =>  `Starting web server on port ...`




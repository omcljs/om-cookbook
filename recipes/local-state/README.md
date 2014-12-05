
1) Create a new Om project using Chestnut

```bash
lein new chestnut local-state -- --om-tools --http-kit
```

2) Use your editor of choice to open the file `local-state/src/cljs/core.cljs`

3) You should remove `(defonce app-state ...)` because it's not needed for this example.

4) Create an Om component with initial state

```clojure
(defcomponent local-state-counter-view [_ owner]
  (init-state [_])
  (render-state [_ state]))
```

5) Set your initial state values

```clojure
(defcomponent local-state-counter-view [_ owner]
  (init-state
   [_]
   {:button-presses 1})
  (render-state [_ state]))
```

6) Create a render state so the initial state can be displayed

```clojure
(defcomponent local-state-counter-view [_ owner]
  (init-state
   [_]
   {:button-presses 1})
  (render-state
   [_ state]
   (dom/div
    (dom/button {} "Click Me")
    (dom/br {})
    (str "Button Presses: "
         (:button-presses state)))))

```

7) Add an onClick JavaScript event to the "Click Me" button. This event should update the local state using `om/update-state!`.

```clojure
(defcomponent local-state-counter-view [_ owner]
  (init-state
   [_]
   {:button-presses 1})
  (render-state
   [_ state]
   (dom/div
    (dom/button
     {:on-click (fn [_]
                  (om/update-state! owner
                                    [:button-presses]
                                    inc))}

     "Click Me")
    (dom/br {})
    (str "Button Presses: "
         (:button-presses state)))))

```

8) Alter or replace the `(defn main [] ...)` function so that the local state component is displayed on the webpage.

```clojure
(defn main []
  (om/root
   local-state-counter-view
   {}
   {:target (. js/document (getElementById "app"))}))
```

9) Start a REPL with `lein repl`

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

10) Call `run` to start the back end and compile your ClojureScript.

```
local-state.server=> (run)
Starting figwheel.
Starting web server on port 10555 .
#<clojure.lang.AFunction$1@336fc74>
local-state.server=> Compiling ClojureScript.
Figwheel: Starting server at http://localhost:3449
Figwheel: Serving files from '(dev-resources|resources)/public'
Compiling "resources/public/js/app.js" from ("src/cljs" "env/dev/cljs")...
Successfully compiled "resources/public/js/app.js" in 18.01 seconds.
notifying browser that file changed:  /js/out/local_state/core.js
```

11) Point your browser to http://localhost:port. You can find the port in the REPL message output =>  `Starting web server on port ...`

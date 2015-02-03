# Problem

You want to do scripted HTML Canvas animations.

# Solution

1. Create a project structure using Leiningen template
[mies](https://github.com/swannodette/mies).

 ```
 $ lein new mies animator
 ```

2. Add om, sablono, and core.async dependencies to project.clj.

 ```clojure
   :dependencies [[org.clojure/clojure "1.6.0"]
                  [org.clojure/clojurescript "0.0-2371"]

                  [om "0.8.0-alpha2"]
                  [sablono "0.2.20"]
                  [org.clojure/core.async "0.1.346.0-17112a-alpha"]]
 ```

3. Add react to index.html, as well as a div to hold our animation.

 ```html
<!DOCTYPE html>
<html>
  <head><title>Hello, Animator!</title></head>
  <body style="background-color:#555152">
      <div id="anim" width="800" height="1000"></div>
      <script src="http://fb.me/react-0.12.2.min.js"></script>
      <script src="out/goog/base.js"></script>
      <script src="animator.js"></script>
      <script>goog.require('animator.core');</script>
  </body>
</html>
 ```

4. Copy animator.cljs and clock.cljs into your project. These
act as a library that you will use, but they are very small, and you might
want to customize them.

5. Now we start working with our own code, in core.cljs.
Create a function that draws something in a canvas.
It should take a drawing-context argument and whatever else it needs.

 ```clojure
 (defn draw-circle
  [context center radius line-width scale {:keys [r g b a]
                                             :or {a 1.0}}]
  (let [h (.-height (.-canvas context))
        center-x (* scale (:x center))
        center-y (- h (* scale (:y center)))
        radius (* scale radius)]
    (set! (. context -strokeStyle) (str "rgba(" r "," g "," b "," a ")"))
    (set! (. context -lineWidth) line-width)
    (.beginPath context)
    ;; x y radius startAngle endAngle counterClockwise?:
    (.arc context center-x center-y radius 0 (* 2 Math/PI) false)
    (.stroke context)))
 ```

6. Create a function that calculates some property based upon the elapsed time.
It should take elapsed-time and whatever else it needs. In this case we are returning
an alpha of 1 (opaque) at the time the animation is to start, and values
linearly decreasing to 0 (transparent) at the time the animation is to end.

 ```clojure
 (defn alpha
  [elapsed-time delay duration]
  (* .001 (- (+ delay duration) elapsed-time)))
 ```

7. Now create our update function using the two functions we have already defined.
It must take elapsed-time and canvas, followed by a map containing all the parameters.

 ```clojure
(defn fading-circle-update
  [elapsed-time canvas {:keys [center radius line-width scale color delay duration] :as opts}]
  (let [context (.getContext canvas "2d")
        a (alpha elapsed-time delay duration)]
    (when (>= elapsed-time delay)
      (.clearRect context 0 0 (.-width canvas) (.-height canvas))
      (draw-circle context center radius line-width scale (merge color {:a a})))))
 ```

8. Define a vector of your own animations.
Each animation is a map containing an update function
and all the parameters that the update function needs.
Each animation will run in its own canvas element,
with the later ones in the vector rendering in front of the earlier ones.

 ```clojure
(defonce animations [{:update fading-circle-update
                      :center {:x 400 :y 200}
                      :radius 75
                      :line-width 2
                      :scale 1
                      :color {:r 0 :g 255 :b 0}
                      :delay 0
                      :duration 2000}

                     {:update fading-circle-update
                      :center {:x 450 :y 250}
                      :radius 50
                      :line-width 2
                      :scale 1
                      :color {:r 255 :g 0 :b 0}
                      :delay 250
                      :duration 1250}

                     {:update fading-circle-update
                      :center {:x 200 :y 340}
                      :radius 25
                      :line-width 2
                      :scale 1
                      :color {:r 255 :g 255 :b 0}
                      :delay 750
                      :duration 1250}

                     {:update fading-circle-update
                      :center {:x 100 :y 100}
                      :radius 50
                      :line-width 2
                      :scale 1
                      :color {:r 255 :g 0 :b 0}
                      :delay 1000
                      :duration 1000}])
 ```

9. Now let's create a component that builds an animator instance
and passes it the required start-time parameter:

 ```clojure
(defn example
  [cursor owner opts]
  (reify
    om/IRender
    (render
     [_]
     (dom/div #js {:width "800px" :height "500px"}

              (om/build animator
                        cursor
                        {;; You must pass a start-time in the state map.
                         ;; In this case we want the animation timeline
                         ;; to begin right away, so we use the current time
                         ;; as reported by the browser:
                         :state {:start-time (.now (.-performance js/window))}

                         ;; You may pass a clock-interval in milliseconds
                         ;; in the opts map (default is 10):
                         :opts {:clock-interval 20}})))))

  ```

6. That's it. Run ``` lein cljsbuild once ``` in
   ```/recipes/animator``` and open ```index.html``` in a
   browser of your choice. You should see the animations.

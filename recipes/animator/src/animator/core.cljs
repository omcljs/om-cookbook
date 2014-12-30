(ns animator.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [animator.animator :refer [animator]]))

(enable-console-print!)


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Define your own update function that draws something
;; into a canvas based on the elapsed time.
;; The animator knows nothing of its internals.


;; First, two helper functions:

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

(defn alpha
  [elapsed-time delay duration]
  (* .001 (- (+ delay duration) elapsed-time)))

;; Here's the example update function.
;; elapsed-time and canvas are provided by the animator;
;; everything in the opts map must come from your animations:

(defn fading-circle-update
  [elapsed-time canvas {:keys [center radius line-width scale color delay duration] :as opts}]
  (let [context (.getContext canvas "2d")
        a (alpha elapsed-time delay duration)]
    (when (>= elapsed-time delay)
      (.clearRect context 0 0 (.-width canvas) (.-height canvas))
      (draw-circle context center radius line-width scale (merge color {:a a})))))
;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Define a vector of your own animations.
;; Each animation is a map containing an update function
;; and all the parameters that the update function needs.
;; Each animation will run in its own canvas element,
;; with the later ones in the vector rendering in front of the earlier ones.

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
;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


;; Create the example component.
;; Its render method will build an animator instance
;; and pass it the required start-time parameter:

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


;; I could have passed the animations vector as another member of
;; the opts map, and this example would work identically.
;; Passing it as app-state, as I do below,
;; lets me imagine user interactions modifying the animations
;; and resetting the start-time to run them again...

(om/root example
         (atom animations)
         {:target (. js/document (getElementById "anim"))})


(ns knob.core
  (:require-macros [cljs.core.async.macros :refer [go go-loop]])
  (:require [om.core :as om :include-macros true]
            [om-tools.dom :as dom :include-macros true]
            [om-tools.core :refer-macros [defcomponent]]))

(defonce app-state
  (atom
   {:panel {:bg-color "#22313F"
            :font-color "white"}
    :knobs [{:id "a" :deg 0 :size 10 :color "#34495E" :border "#22A7F0"}
            {:id "b" :deg 80 :size 20 :color "#22313F" :border "#22A7F0"}
            {:id "c" :deg 180 :size 30 :color "#4B77BE" :border "#22A7F0"}
            {:id "d" :deg 350 :size 40 :color "#446CB3" :border "#22A7F0"}]}))

(defn quadrant [y x rect]
  (let [half #(-> % (* 0.5) int)
        top (.-top rect)
        bottom (.-bottom rect)
        left (.-left rect)
        right (.-right rect)
        x-half (half (- right left))
        x (- x left)
        y-half (half (- bottom top))
        y (- y top)
        y-pos (if (<= y y-half) :up :down)
        x-pos (if (>= x x-half) :right :left)]
    [y-pos x-pos]))

(defn direction
  [cursor y quadrant]
  (let [state @cursor
        {:keys [last-quadrant last-y]} state
        up? (< y last-y)
        direction [up? last-quadrant quadrant]]
    
    (condp = direction
      
      ;; increase top left to right
      [true [:up :left] [:up :left]] inc
      [false [:up :left] [:up :right]] inc
      [true [:up :left] [:up :right]] inc
      [false [:up :right] [:up :right]] inc
      [false [:up :right] [:down :right]] inc
      
      ;; increase bottom right to left
      [false [:down :right] [:down :right]] inc
      [false [:down :right] [:down :left]] inc
      [true [:down :left] [:down :left]] inc
      [false [:down :left] [:down :left]] dec
      
      ;; decrease bottom left to right
      [false [:down :left] [:down :right]] dec      
      [true [:down :right] [:down :right]] dec
      [true [:down :right] [:up :right]] dec
      
      ;; decrease up right to left
      [true [:up :right] [:up :right]] dec
      [true [:up :right] [:up :left]] dec
      [false [:up :left] [:up :left]] dec      
      
      identity)))

(defn mouse-move! [cursor evt]
  (go-loop [state @cursor]
    (when (:mouse-down? state)
      (let [dk :deg
            yk :last-y
            lqk :last-quadrant
            rect (-> js/document
                     (.getElementById (:id state))
                     .getBoundingClientRect)
            y (.-clientY evt)
            x (.-clientX evt)
            q (quadrant y x rect)
            f (direction cursor y q)]
        (om/transact! cursor
                      (fn [state]
                        (-> state
                            (update-in [dk]
                                       (fn [deg]
                                         (if (and (<= deg 350)
                                                  (>= deg 0))
                                           (f deg))))
                            (assoc-in [lqk] q)
                            (assoc-in [yk] y))))))))

(defn mouse-down! [cursor evt]
  (go
    (om/update! cursor [:mouse-down?] (-> evt .-button zero?))))

(defn mouse-up! [cursor evt]
  (go
    (om/update! cursor [:mouse-down?] false)))
  
(defcomponent knob-view [cursor _]
  (did-mount
   [_]
   (let [node (-> js/document
                  (.getElementById (:id cursor)))]
     (-> node .-onmousemove (set! #(mouse-move! cursor %)))
     (-> node .-onmousedown (set! #(mouse-down! cursor %)))
     (-> node .-onmouseup (set! #(mouse-up! cursor %)))))
  (render
   [_]
   (let [{:keys [id deg size color border]} cursor
         rotate (str "rotate(" deg "deg)")
         width (* 1.5 size)
         height width
         padding (* size 0.30)
         stick-width (* width 0.35)
         stick-margin-top (* padding 2)
         stick-margin-left (str "-" (* padding 0.5))
         percent (-> (/ deg 350) (* 100) int)]
     (dom/div
      (dom/h2  (str percent "%"))
      (dom/div {:id id
                :style {:margin "20px"
                        :border-color border
                        :border-style "solid"
                        :border-width "5px"
                        :padding padding
                        :border-radius "50%"
                        :transform rotate
                        :width width
                        :height height
                        :background-color color}}
               (dom/div {:style {:margin-top stick-margin-top
                                 :margin-left stick-margin-left
                                 :padding-right stick-width
                                 :padding-left stick-width
                                 :height "15%"
                                 :border-radius "35%"
                                 :background-color "white"
                                 :border-color "black"
                                 :border-style "solid"
                                 :border-width "1px"
                                 :float "left"}}))))))

(defcomponent main-view [app owner]
  (render
   [_]
   (dom/div {:style {:background-color (-> app :panel :bg-color)
                     :color (-> app :panel :font-color)
                     :padding "10px"}}
            (om/build-all knob-view (:knobs app)))))
            
(defn main []
  (om/root
   main-view
   app-state
   {:target (. js/document (getElementById "app"))}))

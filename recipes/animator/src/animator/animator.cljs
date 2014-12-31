(ns animator.animator
  (:require [om.core :as om :include-macros true]
            [sablono.core :refer-macros [html]]
            [cljs.core.async :refer [<!]]
            [animator.clock :refer [clock]])
  (:require-macros [cljs.core.async.macros :refer [go-loop]]))


(defn canvas-id
  [index]
  (str "animator-canvas-" index))

(defn get-canvas
  [index]
  (. js/document (getElementById (canvas-id index))))

(defn z-level
  [index]
  (* 2 (inc index)))

(defn make-canvas
  [index]
  [:canvas
   {:id (canvas-id index)
    :style #js {:position "absolute" :left "0px" :top "0px"
                :z-index (z-level index)}
    :width "800px" :height "400px"}])



(defn animator
  [animations owner {:keys [clock-interval] :as props}]
  (reify
    om/IInitState
    (init-state
     [this]
     {:clock (clock (or clock-interval 10))})
   ;; :start-time must be injected

    om/IWillMount
    (will-mount
     [_]
     (let [[clock] (om/get-state owner :clock)]
       (go-loop []
                (let [now (<! clock)
                      elapsed-time (- now (om/get-state owner :start-time))]
                  (om/set-state! owner :elapsed-time elapsed-time))
                (recur))))

    om/IRenderState
    (render-state
     [_ {:keys [elapsed-time] :as state}]
     (doseq [index (range (count animations))]
       (let [animation (nth animations index)
             canvas (get-canvas index)
             update (:update animation)]
         (when canvas
           (update elapsed-time canvas animation))))

     (html
      [:div {}
       (map make-canvas (range (count animations)))]))))

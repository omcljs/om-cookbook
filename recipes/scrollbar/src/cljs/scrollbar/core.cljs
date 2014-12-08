(ns scrollbar.core
  (:require-macros [cljs.core.async.macros :refer [go go-loop]])
  (:require [om.core :as om :include-macros true]
            [om-tools.dom :as dom :include-macros true]
            [om-tools.core :refer-macros [defcomponent]]))


(def app-state 
  (atom {:scrollbars []}))

(defn add-sample-scrollbar! [cursor]
  (let [id (gensym)
        sb {:scroller-id (str "scroller" id)
            :slider-track-id (str "slider-track" id)
            :scrollbar-id (str "scrollbar" id)
            :scrollbar-height (rand-nth (range 40 200))
            :scrollbar 100
            :scrolling false}]
    (om/transact! cursor
                  [:scrollbars]
                  (fn [coll]
                    (conj coll sb)))))

(defn scrollbar-mouse-down! [cursor]
  (go 
    (om/update! cursor [:scrolling] true)))

(defn scrollbar-mouse-up! [cursor]
  (go 
    (om/update! cursor [:scrolling] false)))

(defn scrollbar-percent! [cursor percent]
  (om/update! cursor [:scrollbar] percent))

(defn scrollbar-mouse-move! [evt cursor]
  (go-loop [state @cursor
            scroller-node (-> js/document
                              (.getElementById (:scroller-id state)))
            slider-track (-> js/document
                             (.getElementById (:slider-track-id state)))
            y (- (.-clientY evt) 8)
            yp (str y "px")
            true-offset (-> slider-track .getBoundingClientRect .-top)
            true-y (- y true-offset)
            full (:scrollbar-height state)
            percent (->> (/ true-y full)
                         (* 100)
                         (- 100)
                         int)]                        
    (when (and (:scrolling state)
               (>= 100 percent)
               (>= percent 0))
      (-> scroller-node
          .-style.top
          (set! yp))
      (scrollbar-percent! cursor percent))))

(defcomponent scrollbar-view [cursor _]
  (did-mount
   [_]
   (let [scroller-node (-> js/document
                           (.getElementById (:scroller-id cursor)))
         scrollbar-node (-> js/document
                            (.getElementById (:scrollbar-id cursor)))]
     ;; set scrolling
     (doseq [node [scroller-node
                   scrollbar-node]]
       (-> node
           .-onmousedown
           (set! (fn [_] (scrollbar-mouse-down! cursor)))))

     ;; set not scrolling
     (doseq [node [scroller-node
                   scrollbar-node]]       
       (-> node
           .-onmouseup
           (set! (fn [_] (scrollbar-mouse-up! cursor)))))

     ;; set movement listener
     (doseq [node [scroller-node
                   scrollbar-node]]       
       (-> node
           .-onmousemove
           (set! (fn [evt] (scrollbar-mouse-move! evt cursor)))))))         
  (render
   [_]
   (let [height-px (str (:scrollbar-height cursor) "px")]
     (dom/span
      (dom/b (str (:scrollbar cursor) "%"))
      (dom/div {:id (:scrollbar-id cursor)
                :style {:width "20px"
                        :height height-px
                        :padding "8px 2px 8px 2px"
                        :border-width "1px"
                        :border-color "#000"
                        :border-style "solid"
                        :background-color "hsl(190,15%,90%)"}}             
               (dom/div {:id (:slider-track-id cursor)
                         :style {:float "left"
                                 :padding "1px"
                                 :height height-px
                                 :margin-left "9px"
                                 :background-color "#000"}})            
               (dom/div {:id (:scroller-id cursor)
                         :style {:width "18px"
                                 :height "10px"
                                 :position "absolute"
                                 :background-color "hsl(195,100%,50%)"
                                 :border-width "1px"
                                 :border-style "solid"
                                 :border-color "#000"}}))))))

(defcomponent main-view [app owner]
  (will-mount
   [_]
   (dotimes [_ 10]
     (add-sample-scrollbar! app)))
  (render
   [_]
   (dom/table
    (dom/tr
     (for [sb (:scrollbars app)]
       (dom/td {:style {:padding 30}}
               (om/build scrollbar-view sb)))))))

(defn main []
  (om/root
   main-view
   app-state
   {:target (. js/document (getElementById "app"))}))

(ns dimple-bar-chart.core
  (:require [om.core :as om :include-macros true]
            [sablono.core :as html :refer-macros [html]]))

(enable-console-print!)

(defonce app-state (atom {:chart {:div {:width "100%" :height 600}
                                  :data [{:value 240000 :timestamp "2014-01-01"}
                                         {:value 260000 :timestamp "2014-02-01"}
                                         {:value 290000 :timestamp "2014-03-01"}
                                         {:value 70000  :timestamp "2014-04-01"}
                                         {:value 100000 :timestamp "2014-05-01"}
                                         {:value 120000 :timestamp "2014-06-01"}
                                         {:value 240000 :timestamp "2014-07-01"}
                                         {:value 220000 :timestamp "2014-08-01"}
                                         {:value 360000 :timestamp "2014-09-01"}
                                         {:value 260000 :timestamp "2014-10-01"}
                                         {:value 250000 :timestamp "2014-11-01"}
                                         {:value 190000 :timestamp "2014-12-01"}]}}))

(defn get-div-dimensions
  "Get width and height of a div with a specified id."
  [id]
  (let [e (.getElementById js/document id)
        x (.-clientWidth e)
        y (.-clientHeight e)]
    {:width x :height y}))

(defn- draw-chart [data div {:keys [id chart]}]
  (let [{:keys [width height]}    div
        {:keys [bounds plot
                x-axis y-axis]}   chart
        Chart                     (.-chart js/dimple)
        svg                       (.newSvg js/dimple (str "#" id) width height)
        dimple-chart              (.setBounds (Chart. svg) (:x bounds) (:y bounds) (:width bounds) (:height bounds))
        x                         (.addCategoryAxis dimple-chart "x" x-axis)
        y                         (.addMeasureAxis dimple-chart "y" y-axis)
        s                         (.addSeries dimple-chart nil plot (clj->js [x y]))]
    (aset s "data" (clj->js data))
    (.addLegend dimple-chart "5%" "10%" "20%" "10%" "right")
    (.draw dimple-chart)))

(defn bar-chart [cursor owner {:keys [id chart] :as opts}]
  (reify
    om/IWillMount
    (will-mount [_]
      ;; Add event listener that will update width & height when window is resized
      (.addEventListener js/window
                         "resize" (fn []
                                    (let [{:keys [width height]} (get-div-dimensions id)]
                                      (om/update! cursor :div {:width width :height height})))))
    om/IRender
    (render [_]
      (let [{:keys [width height]} (:div cursor)]
        (html
         [:div {:id id :width width :height height}])))
    om/IDidMount
    (did-mount [_]
      (when-let [data (seq (:data cursor))]
        (draw-chart data (:div cursor) opts)))
    om/IDidUpdate
    (did-update [_ _ _]
      (let [{:keys [width height]} (:div cursor)]
        (let [n (.getElementById js/document id)]
          (while (.hasChildNodes n)
            (.removeChild n (.-lastChild n))))
        (when-let [data (seq (:data cursor))]
          (draw-chart data (:div cursor) opts))))))

(om/root
 (fn [app owner]
   (reify
     om/IRender
     (render [_]
       (html
        [:div
         [:h1 "Dimple vertical bar chart"]
         (om/build bar-chart (:chart app) {:opts {:id "chart"
                                                  :chart {:bounds {:x "5%"
                                                                   :y "15%"
                                                                   :width "80%"
                                                                   :height "60%"}
                                                          :plot js/dimple.plot.bar
                                                          :x-axis "timestamp"
                                                          :y-axis "value"}}})]))))
 app-state {:target (. js/document (getElementById "app"))})

(ns views.core
  (:require [om-bootstrap.button :as b]
            [om-bootstrap.table :refer [table]]
            [om-bootstrap.panel :as p]
            [om-tools.dom :as d :include-macros true]
            [om.core :as om :include-macros true]
            [sablono.core :as html :refer-macros [html]]))

(enable-console-print!)

(defonce app-state (atom {:view {:selected :table}
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
                                 {:value 190000 :timestamp "2014-12-01"}]}))

(defmulti view (fn [cursor owner opts] (-> cursor :view :selected)))

(defmethod view :table [cursor owner opts]
  (om/component
   (table {:striped? true :bordered? true :hover? true}
          (d/thead
           (d/tr
            (d/th "Timestamp")
            (d/th "Value")))
          (d/tbody
           (for [v (:data cursor)]
             (d/tr
              (d/td (:timestamp v))
              (d/td (:value v))))))))

(defmethod view :panels [cursor owner opts]
  (om/component
   (d/div
    (for [v (:data cursor)]
      (p/panel {}
               (d/p
                (d/p (str "Timestamp: " (:timestamp v)))
                (d/p (str "Value: " (:value v)))))))))

(defn toggle [cursor owner opts]
  (om/component
   (b/toolbar {}
              (b/button {:active? (when (= :table (-> cursor :selected)) true)
                         :on-click (fn [_] (om/update! cursor :selected :table))}
                        "Table")
              (b/button {:active? (when (= :panels (-> cursor :selected)) true)
                         :on-click (fn [_] (om/update! cursor :selected :panels))}
                        "Panels"))))

(om/root
 (fn [app owner]
   (reify
     om/IRender
     (render [_]
       (html
        [:div.col-md-6
         [:h1 "Views"]
         [:div
          [:div {:style {:padding-bottom "3px"}}
           ;; Button to toggle the view
           (om/build toggle (:view app))]
          ;; View component
          (om/build view app)]]))))
 app-state {:target (. js/document (getElementById "app"))})

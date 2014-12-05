(ns input-validation.core
  (:require [om.core :as om :include-macros true]
            [om-tools.dom :as dom :include-macros true]
            [om-tools.core :refer-macros [defcomponent]]))

(defonce app-state (atom {:user-input "some value"}))

(defcomponent user-input-view [app owner]
  (render
   [_]
   (let [ref "user-input"
         k :user-input]
     (dom/input {:ref ref
                 :value (k app)
                 :on-change (fn [_]
                              (let [this (om/get-node owner ref)]
                                (om/update! app [k] (.-value this))))}))))


(defn valid-input?  [x]
  (> 10 (count x)))

(defcomponent validation-box-view [app _]
  (render
   [_]
   (let [valid-color "green"
         invalid-color "red"
         color (if (valid-input? (:user-input app))
                 valid-color
                 invalid-color)]     
     (dom/div {:style {:padding "10px;"
                       :background-color color}}
              (om/build user-input-view app)))))
     
(defn main []
  (om/root
   validation-box-view
   app-state
   {:target (. js/document (getElementById "app"))}))

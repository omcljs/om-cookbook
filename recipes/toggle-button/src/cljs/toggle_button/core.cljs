(ns toggle-button.core
  (:require [om.core :as om :include-macros true]
            [om-tools.dom :as dom :include-macros true]
            [om-tools.core :refer-macros [defcomponent]]))


(defonce app-state (atom {:toggle true}))

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
            
(defn main []
  (om/root
   toggle-button-view
   app-state
   {:target (. js/document (getElementById "app"))}))

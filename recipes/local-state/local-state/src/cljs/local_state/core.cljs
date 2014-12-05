(ns local-state.core
  (:require [om.core :as om :include-macros true]
            [om-tools.dom :as dom :include-macros true]
            [om-tools.core :refer-macros [defcomponent]]))

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

(defn main []
  (om/root
   local-state-counter-view
   {}
   {:target (. js/document (getElementById "app"))}))

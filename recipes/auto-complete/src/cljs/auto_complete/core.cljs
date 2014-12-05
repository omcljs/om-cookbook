(ns auto-complete.core
  (:require [om.core :as om :include-macros true]
            [om-tools.dom :as dom :include-macros true]
            [om-tools.core :refer-macros [defcomponent]]
            [clojure.string :as cs]))

(defonce app-state
  (atom {:user-input ""
         :things ["Apple"
                  "ant"
                  "bird"
                  "CAR"
                  "crayon"
                  "cooler"
                  "DVD"
                  "Dirt"
                  "DOG"]}))

(defn partial-complete [x]
  (if-not (empty? x)
    (let [x (cs/lower-case x)
          things (:things @app-state)
          things-lower (map cs/lower-case things)
          things-map (zipmap things things-lower)
          pattern (re-pattern
                   (str "^" x ".*"))
          possible-things (filter #(re-matches pattern (val %))
                                  things-map)]
      (if-let [possible-things (seq possible-things)]
        (map key possible-things)))))

(defcomponent partial-complete-view [app _]
  (render
   [_]
   (dom/div
    (if-let [completions (seq
                          (partial-complete (:user-input app)))]
      (for [thing completions]
        (dom/div {} thing))
      "No Completions."))))
             
(defcomponent user-input [app owner]
  (render
   [_]
   (let [ref "user-input"
         k :user-input]
     (dom/input {:ref ref
                 :on-change (fn [_]
                              (let [this (om/get-node owner ref)
                                    v (.-value this)]
                                (om/update! app k v)))}))))

(defcomponent auto-complete-view [app _]
  (render
   [_]
   (dom/div
    (om/build user-input app)
    (dom/br {})
    (om/build partial-complete-view app))))

(defn main []
  (om/root
   auto-complete-view
   app-state
   {:target (. js/document (getElementById "app"))}))

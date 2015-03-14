(ns auto-complete.core
  (:require [om.core :as om :include-macros true]
            [om-tools.dom :as dom :include-macros true]
            [om-tools.core :refer-macros [defcomponent]]))

(defonce app-state
  (atom {:user-input ""
         :things ["Apple"
                  "ant"
                  "bird"
                  "CAR, Audi"
                  "crayon"
                  "cooler"
                  "DVD"
                  "Dirt"
                  "DOG, Beagle"]}))

(defn partial-complete
  "Takes a needle and looks for it in the haystack.
Return all matches in a set or nil on empty match set.
Empty needle causes empty match set."
  [needle haystack]
  (when-not (empty? needle)
    (let [pattern (js/RegExp. (str ".* " needle ".*|" needle ".*") "i")]
      (not-empty (set (keep #(re-matches pattern %) haystack))))))

(defcomponent partial-complete-view [app _]
  (render
   [_]
   (dom/div
    (if-let [completions (partial-complete (:user-input app) (:things app))]
      (for [thing completions]
        (dom/div {} thing))
      "No Completions."))))


(defcomponent user-input [app owner]
  (render
   [_]
   (dom/input {:value (:user-input app)
               :on-change #(om/update! app :user-input (.. % -target -value))})))

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

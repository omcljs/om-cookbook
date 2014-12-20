(ns boot-setup.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]))

(def app-state (atom {:cong "Very much congratulated!"}))

(defn main
  []
  (om/root
    (fn [app owner]
      (reify
        om/IRender
        (render [_]
          (dom/div nil
            (dom/h1 nil "Yay!")
            (dom/p nil "I did it!!!")
            (dom/p nil (:cong app))))))
    app-state
    {:target js/document.body}))

(defn another-root
  []
  (om/root
    (fn [app owner]
      (reify
        om/IRender
        (render [_]
          (dom/div nil
            (dom/h1 nil "Hello from prototype!")))))
    app-state
    {:target js/document.body}))

(set! (.-onload js/window) main)

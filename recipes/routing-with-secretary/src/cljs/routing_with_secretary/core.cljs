(ns routing-with-secretary.core
  (:require [om.core :as om :include-macros true]
            [om-tools.dom :as dom :include-macros true]
            [om-tools.core :refer-macros [defcomponent]]
            [secretary.core :as sec
             :include-macros true]
            [goog.events :as events]
            [goog.history.EventType :as EventType])
  (:import goog.History))

(sec/set-config! :prefix "#")

(let [history (History.)
      navigation EventType/NAVIGATE]
  (goog.events/listen history
                     navigation
                     #(-> % .-token sec/dispatch!))
  (doto history (.setEnabled true)))

(defcomponent navigation-view [_ _]
  (render
   [_]
   (let [style {:style {:margin "10px;"}}]
     (dom/div style
              (dom/a (assoc style :href "#/") 
                     "Home")
              (dom/a (assoc style :href "#/something") 
                     "Something")
              (dom/a (assoc style :href "#/about") 
                     "About")))))
              
(defcomponent index-page-view [_ _]
  (render
   [_]
   (dom/div
    (om/build navigation-view {})
    (dom/h1 "Index Page"))))
          
(sec/defroute index-page "/" []
  (om/root index-page-view
           {}
           {:target (. js/document (getElementById "app"))}))

(defcomponent something-page-view [_ _]
  (render
   [_]
   (dom/div
    (om/build navigation-view {})
    (dom/h1 "Something Page"))))
          
(sec/defroute something-page "/something" []
  (om/root something-page-view
           {}
           {:target (. js/document (getElementById "app"))}))

(defcomponent about-page-view [_ _]
  (render
   [_]
   (dom/div
    (om/build navigation-view {})
    (dom/h1 "About Page"))))
          
(sec/defroute about-page "/about" []
  (om/root about-page-view
           {}
           {:target (. js/document (getElementById "app"))}))

(defn main []
  (-> js/document
      .-location
      (set! "#/")))

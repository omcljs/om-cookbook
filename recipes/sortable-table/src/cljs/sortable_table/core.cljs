(ns sortable-table.core
  (:require [om.core :as om :include-macros true]
            [om-tools.dom :as dom :include-macros true]
            [om-tools.core :refer-macros [defcomponent]]))

(defonce app-state 
  (atom
   {:table [{:gender "M" :name "person1" :age 100}
            {:gender "F" :name "person2" :age 50}
            {:gender "N/A" :name "person3" :age 10}
            {:gender "F" :name "person4" :age 20}
            {:gender "F" :name "person5" :age 30}
            {:gender "M" :name "person6" :age 80}
            {:gender "N/A" :name "person7" :age 80}]}))

(defn sort-by-*! [k]
  (swap! app-state
         update-in
         [:table]
         (fn [table]
           (sort-by k table))))

(defn sort-by-*-reverse! [k]
  (swap! app-state
         update-in
         [:table]
         (fn [table]
           (reverse
            (sort-by k table)))))

(defcomponent table-view-header [{:keys [header]} _]
  (render
   [_]
   (let [header-name (name header)
         header-kw (keyword header-name)
         attr {:href "javascript:void(0);"
               :style {:margin "5px;"
                       :font-size "30px;"
                       :text-decoration "none;"}}
         arrow-down-event (fn [_]
                            (sort-by-*! header-kw))
         arrow-up-event (fn [_]
                          (sort-by-*-reverse! header-kw))
         arrow-up-attr (assoc attr :on-click arrow-up-event)
         arrow-down-attr (assoc attr :on-click arrow-down-event)
         header-attr {:style {:padding-right "20px;"
                              :font-size "25px;"}}]
     (dom/th header-attr
             (clojure.string/capitalize header-name)
             ;; UTF-8 Up Arrow
             (dom/a arrow-up-attr "↑")
             ;; UTF-8 Down Arrow
             (dom/a arrow-down-attr "↓")))))

(defn header-types []
  (map #(hash-map :header %)
       (-> @app-state
           :table
           first
           keys)))

(defcomponent table-row [row owner]
  (render 
   [_]
   (dom/tr
    (for [value (map val row)]
      (dom/td value)))))

(defcomponent table-view [app _]
  (render
   [_]   
   (dom/table
    (dom/tr
     (om/build-all table-view-header
                   (header-types)))
    (om/build-all table-row
                  (:table app)))))

(defn main []
  (om/root
   table-view
   app-state
   {:target (. js/document (getElementById "app"))}))

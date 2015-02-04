(ns sortable-table.core
  (:require
   [om.core :as om :include-macros true]
   [om-tools.dom :as dom :include-macros true]
   [om-tools.core :refer-macros [defcomponent]]))

(def app-state 
  (atom
   {:table [{:gender "M" :name "person1" :age 100}
            {:gender "F" :name "person2" :age 50}
            {:gender "N/A" :name "person3" :age 10}
            {:gender "F" :name "person4" :age 20}
            {:gender "F" :name "person5" :age 30}
            {:gender "M" :name "person6" :age 80}
            {:gender "N/A" :name "person7" :age 80}]}))

(defcomponent table-view-header [{:keys [header sort-fn]} _] 
  (render
   [_]
   (let [header-name (name header)
         header-kw (keyword header-name)
         attr {:href "javascript:void(0);"
               :style {:margin "5px;"
                       :font-size "30px;"
                       :text-decoration "none;"}
               :on-click sort-fn}
         header-attr {:style {:padding-right "20px;"
                              :font-size "25px;"}}]
     (dom/th header-attr
             (clojure.string/capitalize header-name)
             ;; UTF-8 Up/Down Arrow
             (dom/a attr "⬍")))))

(defcomponent table-row [row owner]
  (render 
   [_]
   (dom/tr
    (for [value (map val row)]
      (dom/td value)))))

(defn sorted-data [key direction data]
  (if key
    ((if direction reverse identity)
     (sort-by key data))
    data))

(defn change-sorting [owner key]
  ;; invert direction on second click
  (when (= (om/get-state owner :sort) key)
    (om/set-state! owner :sort-dir (not (om/get-state owner :sort-dir))))
  (om/set-state! owner :sort key))

(defn header-types [data sort-fn]
  (map #(hash-map :header % :sort-fn (partial sort-fn %))
       (-> data
           first
           keys)))

(defcomponent table-view [data owner]
  ;; Our local state stores the sort key and the sort direction.
  ;; The model data will never be modified, it contains the truth.
  ;; Sorting is just a ephemeral view setting, which is also stored in said view.
  (init-state [_] {:sort :nil
                   :sort-dir nil})
  (render-state
   [_ state]   
   (dom/table
      (dom/tr
       (om/build-all table-view-header (header-types data (partial change-sorting owner))))
      (om/build-all table-row
                    (sorted-data (:sort state)
                                 (:sort-dir state)
                                 data)))))


(defcomponent main-view [app-state]
  (render [_]
          (om/build table-view (:table app-state))))

(defn main []
  (om/root
   main-view
   app-state
   {:target (. js/document (getElementById "app"))}))

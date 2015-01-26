(ns kioo-blog.core
  (:require [kioo.om :refer [set-style set-attr do-> substitute listen] :as kio]
            [kioo.core :refer [handle-wrapper]]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true])
  (:require-macros [kioo.om :refer [defsnippet deftemplate]]))

(def app-state (atom {:heading "My programming blog"
                      :contents  [[:100 "Are We There Yet?"]
                                  [:200 "Simple Made Easy."]
                                  [:300 "The Value of Values"]]
                      :navigation [["home" #(.log js/console %)]
                                   ["blog" #(.log js/console %)]]
                      :footer "Eclipse Public License (c)"}))

(defsnippet menu-view "blog.html" [:.nav-item]
  [[caption func]]
  {[:a] (do-> (kio/content caption)
              (listen :onClick #(func caption)))})

(defsnippet header-view "blog.html" [:header]
  [{:keys [heading navigation]}]
  {[:h1] (kio/content heading)
   [:ul] (kio/content (map menu-view navigation))})

(defsnippet content-view "blog.html" [:section#blog]
  [content]
  {[:article.content] (kio/content (second content))})

(defsnippet footer-view "blog.html" [:footer]
  [{:keys [footer]}]
  {[:h4] (kio/content footer)})

(deftemplate blog-tmpl "blog.html"
  [data]
  {[:header] (substitute (header-view data))
   [:section#blog] (substitute (map content-view (:contents data)))
   [:footer] (substitute (footer-view data))})

(defn template [data] (om/component (blog-tmpl data)))

(defn main []
  (om/root
   template
   app-state
   {:target (.-body js/document)}))

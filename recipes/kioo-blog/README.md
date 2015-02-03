# Om with Kioo Templating

Kioo brings Enlive/Enfocus style templates to React/Om. Instead of building the dom with om/om-tools/sablono, you accept a static html and replace its dom with css-like selectors. Kioo snippets and templates are components that return React/om components, and as such, you'll be working with kioo's api to 'transform' the dom.

For further info on a list of transformations, checkout [kioo](https://github.com/ckirkendall/kioo).

1) Create a new Om project using Chestnut

```bash
lein new chestnut kioo-blog
```

2) Use your editor of choice to open the file `kioo-blog/src/cljs/core.cljs`

3) Add kioo to your project file

```clojure
[kioo "0.4.0" :exclusions [com.facebook/react
                           om]]
```

4) Add Kioo to your project requirements

```clojure
(ns kioo-blog.core
  (:require [kioo.om :refer [set-style set-attr do-> substitute listen] :as kio]
            [kioo.core :refer [handle-wrapper]]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true])
  (:require-macros [kioo.om :refer [defsnippet deftemplate]]))
```

6) Add an initial app-state.

```clojure
(def app-state (atom {:heading "My programming blog"
                      :contents  [[:100 "Are We There Yet?"]
                                  [:200 "Simple Made Easy."]
                                  [:300 "The Value of Values"]]
                      :navigation [["home" #(.log js/console %)]
                                   ["blog" #(.log js/console %)]]
                      :footer "Eclipse Public License (c)"}))
```

7) Initialize a template and replace header/footer

Study `resources/blog.html`. Let's replace menu and header.

```clojure

(defsnippet menu-view "blog.html" [:.nav-item]
  [[caption func]]
  {[:a] (do-> (kio/content caption)
              (listen :onClick #(func caption)))})

(defsnippet header-view "blog.html" [:header]
  [{:keys [heading navigation]}]
  {[:h1] (kio/content heading)
   [:ul] (kio/content (map menu-view navigation))})

(deftemplate blog-tmpl "blog.html"
  [data]
  {[:header] (substitute (header-view data))})

```

What just happened: `header-snippet` read the header tag of `blog.html`. After that, it transformed inidividual selectors, h1, ul, menu with content passed in (from the app-state cursor). Note how transformations can call other snippets, like `menu-view`.

8) Continue transforming content.

```clojure
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
```

9) Start a REPL with `lein repl`

Call `run` to start the back end and compile your ClojureScript. Point your browser to http://localhost:port.

You can find the port in the REPL message output.



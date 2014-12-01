# Problem

You want create different views for the same data and toggle them
depending on user's selection.

# Solution

1. Create a project structure using Leiningen template
[mies](https://github.com/swannodette/mies)

 ```
 $ lein new mies views
 ```

2. We will use [om-boostrap](http://om-bootstrap.herokuapp.com/) to
create table and panels. Add we'll also add Om and
[sablono](https://github.com/r0man/sablono) to our
dependencies:

  ```clojure
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2371"]

                 [racehub/om-bootstrap "0.3.1"]
                 [om "0.8.0-alpha2"]
                 [sablono "0.2.20"]]
  ```

3. Let's add React and Boostrap CSS to our html file:

  ```
   <html>
    <head>
     <link href="https://netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css"
         rel="stylesheet"/>
    </head>
    <body>
      <div id="app"></div>
      <script src="http://fb.me/react-0.11.1.js"></script>
      <script src="out/goog/base.js" type="text/javascript"></script>
      <script src="views.js" type="text/javascript"></script>
      <script type="text/javascript">goog.require("views.core");</script>
    </body>
  </html>
  ```

4. We're going to use a toolbar with two buttons as our view toggle:

  ```clojure
  (defn toggle [cursor owner opts]
    (om/component
     (b/toolbar {}
                (b/button {:active? (when (= :table (-> cursor :selected)) true)
                           :on-click (fn [_] (om/update! cursor :selected :table))}
                          "Table")
                (b/button {:active? (when (= :panels (-> cursor :selected)) true)
                           :on-click (fn [_] (om/update! cursor :selected :panels))}
                          "Panels"))))
  ```

5. Depending on the view selected we'll want to build a different
component. We can use a multimethod for that:

  ```clojure
  (defmulti view (fn [cursor owner opts] (-> cursor :view :selected)))
  ```

  If the view selected is a table, we'll build this:

  ```clojure
  (defmethod view :table [cursor owner opts]
    (om/component
     (table {:striped? true :bordered? true :hover? true}
            (d/thead
             (d/tr
              (d/th "Timestamp")
              (d/th "Value")))
            (d/tbody
             (for [v (:data cursor)]
               (d/tr
                (d/td (:timestamp v))
                (d/td (:value v))))))))
  ```

  Or if it's panels, then we'll build this:

  ```clojure
  (defmethod view :panels [cursor owner opts]
    (om/component
     (d/div
      (for [v (:data cursor)]
        (p/panel {}
                 (d/p
                  (d/p (str "Timestamp: " (:timestamp v)))
                  (d/p (str "Value: " (:value v)))))))))
  ```

6. That's it. Run `lein cljsbuild once`, open up `index.html` in
your browser and try out selecting different views.

  Multimethods are a great way to build different components based on
  some criteria.

# Problem

You want to create a simple bar chart using [dimple.js](http://dimplejs.org/).

# Solution

1. Create a project structure using Leiningen template
[mies](https://github.com/swannodette/mies)

 ```
 $ lein new mies dimple-bar-chart
 ```

2. Add om and [sablono](https://github.com/r0man/sablono) to dependencies

 ```clojure
 :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2371"]

                 [om "0.8.0-alpha2"]
                 ;; Hiccup style templating for React in ClojureScript
                 [sablono "0.2.20"]]
 ```

3. Add react, d3 and dimple to index.html

 ```html
 <html>
   <body>
     <script src="http://fb.me/react-0.11.1.js"></script>
     <script src="http://d3js.org/d3.v3.min.js"></script>
     <script src="http://dimplejs.org/dist/dimple.v2.1.0.min.js"></script>
     <script src="out/goog/base.js" type="text/javascript"></script>
     <script src="dimple_bar_chart.js" type="text/javascript"></script>
     <script type="text/javascript">goog.require("dimple_bar_chart.core");</script>
   </body>
 </html>

 ```
4. Code to create a vertical bar chart using JavaScript looks like
this:

 ```JavaScript
 <div id="app">
   <script type="text/javascript">
     var data = [{ "value":240000, "timestamp":"2014-01-01"},
                 { "value":260000, "timestamp":"2014-02-01"},
                 { "value":290000, "timestamp":"2014-03-01"},
                 { "value":70000,  "timestamp":"2014-04-01"}];

     var svg = dimple.newSvg("#app", 600, 400);
     var myChart = new dimple.chart(svg, data);
     myChart.setBounds("5%", "15%", "80%", "60%")
     var x = myChart.addCategoryAxis("x", "timestamp");
     x.addOrderRule("Date");
     myChart.addMeasureAxis("y", "value");
     myChart.addSeries(null, dimple.plot.bar);
     myChart.draw();
   </script>
   </div>
 ```

 This creates a very simple bar chart. We're going to use interop to do
 the same in ClojureScript:

 ```Clojure
 (defn- draw-chart [data div {:keys [id chart]}]
   (let [{:keys [width height]}    div
         {:keys [bounds plot
                 x-axis y-axis]}   chart
         Chart                     (.-chart js/dimple)
         svg                       (.newSvg js/dimple (str "#" id) width height)
         dimple-chart              (.setBounds (Chart. svg) (:x bounds) (:y bounds) (:width bounds) (:height bounds))
         x                         (.addCategoryAxis dimple-chart "x" x-axis)
         y                         (.addMeasureAxis dimple-chart "y" y-axis)
         s                         (.addSeries dimple-chart nil plot (clj->js [x y]))]
     (aset s "data" (clj->js data))
      (.addLegend dimple-chart "5%" "10%" "20%" "10%" "right")
    (.draw dimple-chart)))
 ```

 We've provided some options around the height, width and margins
 making the chart a little bit more robust.

5. Now let's create a component that builds this chart:

 We want to generate an empty div that d3/dimple will use for the
 SVG. Width and height are stored in our application state:

 ```Clojure
  om/IRender
  (render [_]
    (let [{:keys [width height]} (:div cursor)]
      (html
       [:div {:id id :width width :height height}])))
 ```
 Once the component is mounted, we can tell dimple to draw the chart:

 ```clojure
  om/IDidMount
  (did-mount [_]
   (when-let [data (seq (:data cursor))]
    (draw-chart data (:div cursor) opts)))
 ```

 But we also want to update the chart whenever the data changes, so we
 want to do this:

 ```clojure
  om/IDidUpdate
      (did-update [_ _ _]
        (let [{:keys [width height]} (:div cursor)]
          (let [n (.getElementById js/document id)]
            (while (.hasChildNodes n)
              (.removeChild n (.-lastChild n))))
          (when-let [data (seq (:data cursor))]
            (draw-chart data (:div cursor) opts))))
 ```
 We need to remove the old chart and let dimple create a new one.

 One last step: when we resize the window, we'd like the chart to
 resize as well. This can be done by adding an event listener:

 ```clojure
  om/IWillMount
     (will-mount [_]
       (.addEventListener js/window
                          "resize" (fn []
                                     (let [{:keys [width height]} (get-div-dimensions id)]
                                       (om/update! cursor :div {:width width :height height})))))
  ```
6. That's it. Run ``` lein cljsbuild once ``` in
   ```/recipes/dimple-bar-chart``` and open ```index.html``` in a
   browser of your choice. You should see the chart.

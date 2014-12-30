(ns animator.clock
  (:require [cljs.core.async :refer [put! <! chan timeout]])
  (:require-macros [cljs.core.async.macros :refer [go-loop]]))

(defn clock
  "Create a channel which emits the current time every interval milliseconds.
  Any value written to start/stop will start/stop the messages."
  [interval]
  (let [out (chan)
        start (chan)
        stop (chan)]
    (go-loop [running? true]
             (let [t (timeout interval)
                   [_ ch] (alts! [stop t start])]
               (when running?
                  (let [now (.now (.-performance js/window))]
                    (>! out now)))
               (condp = ch
                 stop (recur false)
                 t (recur running?)
                 start (recur true))))
    [out start stop]))

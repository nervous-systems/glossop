(ns glossop.util
  (:refer-clojure :exclude [reduce into])
  #?@(:clj
      ((:require [clojure.core.async :as async :refer [go]]
                 [glossop.core :refer [<? go-catching]]))
      :cljs
      ((:require [cljs.core.async :as async]
                 [glossop.core :refer [<?]])
       (:require-macros [cljs.core.async.macros :refer [go]]
                        [glossop.macros :refer [go-catching]]))))

(defn keyed-merge
  "Merge a map of channel->opaque into a single channel containing a sequence of
  pairs ([opaque value] ...).  Each pair associates an input value to the result
  of reading from the corresponding channel.  As with merge, the returned
  channel will be unbuffered by default, or a buf-or-n can be supplied. The
  channel will close after all the source channels have closed."
  ([ch->tag] (keyed-merge ch->tag nil))
  ([ch->tag buf-or-n]
   (let [out (async/chan buf-or-n)]
     (go
       (loop [cs (vec (keys ch->tag))]
         (if (pos? (count cs))
           (let [[v c] (async/alts! cs)]
             (if (nil? v)
               (recur (filterv #(not= c %) cs))
               (do (async/>! out [(ch->tag c) v])
                   (recur cs))))
           (async/close! out))))
     out)))

(defn onto-chan?
  "A variation of async/onto-chan indicates whether all puts were
  accepted. Never closes the destination channel.  The returned channel will
  resolve to a false value if the destination channel closes, or true if all
  puts are completed."
  ([ch coll]
   (async/go
     (loop [vs (seq coll)]
       (if vs
         (when (async/>! ch (first vs))
           (recur (next vs)))
         true)))))

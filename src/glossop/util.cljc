(ns glossop.util
  (:refer-clojure :exclude [into reduce])
  (:require [glossop.core :refer [throw-err #?@ (:clj [<? go-catching])]]
            #? (:clj
                [clojure.core.async :as async :refer [go]]
                :cljs
                [cljs.core.async :as async :refer [go]]))
  #? (:cljs
      (:require-macros [cljs.core.async.macros :refer [go]]
                       [glossop.macros :refer [<? go-catching]])))

;; TODO have these guys optionally combine errors, rather than stopping on the
;; first

(defn reduce
  "async/reduce with short-circuiting on the first error"
  [f init ch]
  (go-catching
    (loop [ret init]
      (let [v (<? ch)]
        (if (nil? v)
          ret
          (recur (f ret v)))))))

(defn into
  "async/into with short-circuiting on the first error"
  [coll ch]
  (reduce conj coll ch))

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
   (go
     (loop [vs (seq coll)]
       (if vs
         (when (async/>! ch (first vs))
           (recur (next vs)))
         true)))))

(defn close-with! [ch v]
  (async/put! ch v)
  (async/close! ch))

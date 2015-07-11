(ns glossop.core
  (:require #?(:clj
               [clojure.core.async :as async]
               :cljs
               [cljs.core.async :as async]))
  #?(:cljs (:require-macros [cljs.core.async.macros])))

(defn throw-err [e]
  (when (instance? #?(:clj Throwable :cljs js/Error) e)
    (throw e))
  e)

#?(:clj
   (do
     (defmacro <? [ch]
       `(throw-err (async/<! ~ch)))

     (defn <?! [ch]
       (throw-err (async/<!! ch)))

     (defmacro go-catching [& body]
       `(async/go
          (try
            ~@body
            (catch Exception e#
              e#))))))

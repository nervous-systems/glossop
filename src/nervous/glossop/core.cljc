(ns glossop.core
  (:require #?(:clj
               [clojure.core.async :as async]
               :cljs
               [cljs.core.async :as async])))

(defn throw-err [e]
  (when (instance? #?(:clj Throwable :cljs js/Error) e)
    (throw e))
  e)

#?(:clj
   (defmacro <? [ch]
     `(throw-err (async/<! ~ch)))
   :cljs
   (defn <? [ch]
     (throw-err (async/<! ch))))

#?(:clj
   (do
     (defn <?! [ch]
       (throw-err (async/<!! ch)))

     (defmacro go-catching [& body]
       `(async/go
          (try
            ~@body
            (catch Exception e#
              e#))))))

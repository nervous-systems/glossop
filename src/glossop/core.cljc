(ns glossop.core
  (:require #? (:clj
                [clojure.core.async :as async]
                :cljs
                [cljs.core.async]))
  #? (:cljs (:require-macros [glossop.core])))

(defn error? [e]
  (instance? #? (:clj Exception :cljs js/Error) e))

(defn throw-err [e]
  (when (error? e)
    (throw e))
  e)

#? (:clj
    (do
      (defmacro <? [ch]
        (if (:ns &env)
          `(throw-err (cljs.core.async/<! ~ch))
          `(throw-err (async/<! ~ch))))

      (defn <?! [ch]
        (throw-err (async/<!! ch)))

      (defmacro go-catching [& body]
        (if (:ns &env)
          `(cljs.core.async.macros/go
             (try
               ~@body
               (catch js/Error e#
                 e#)))
          `(async/go
             (try
               ~@body
               (catch Exception e#
                 e#)))))))

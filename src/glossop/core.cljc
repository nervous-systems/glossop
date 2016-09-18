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
    (defmacro <? [ch]
      (if (:ns &env)
        `(throw-err (cljs.core.async/<! ~ch))
        `(throw-err (async/<! ~ch)))))

#? (:clj
    (defn <?! [ch]
      (throw-err (async/<!! ch))))

#? (:clj
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
               e#))))))

(defn assoc-when [m & kvs]
  (persistent!
   (reduce
    (fn [acc [k v]]
      (cond-> acc (not (nil? v)) (assoc! k v)))
    (transient m)
    (partition 2 kvs))))

(defn traduce [f m]
  (persistent! (reduce-kv f (transient {}) m)))

;; Taken from plumbing
#? (:clj
    (defmacro for-map
      ([seq-exprs key-expr val-expr]
       `(for-map ~(gensym "m") ~seq-exprs ~key-expr ~val-expr))
      ([m-sym seq-exprs key-expr val-expr]
       `(let [m-atom# (atom (transient {}))]
          (doseq ~seq-exprs
            (let [~m-sym @m-atom#]
              (reset! m-atom# (assoc! ~m-sym ~key-expr ~val-expr))))
          (persistent! @m-atom#)))))

#? (:clj
    (defmacro pred-> [v & clauses]
      (let [g       (gensym)
            clauses (mapcat (fn [[pred conseq]] [(list pred g) conseq])
                            (partition 2 clauses))]
        `(let [~g ~v]
           (cond-> ~g ~@clauses)))))

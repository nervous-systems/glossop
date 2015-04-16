(ns glossop
  (:require [clojure.core.async :as async]))

;; There's very similar junk in plumbing, revise
(defn mapkv [kf vf m]
  (into {} (map (fn [[k v]] [(kf k) (vf v)]) m)))

(defn mapkeys [f m]
  (mapkv f identity m))

(defn mapvals [f m]
  (mapkv identity f m))

(defn keyed-map
  "Like group-by, but assumes unique keys.
  (keyed-map :foo #(dissoc % :foo) [{:foo 1 :bar 2) {:foo 2 :bar 3}])
  -> {1 {:bar 2} {2 {:bar 3}}}"
  ([key-fn s] (keyed-map key-fn identity s))
  ([key-fn val-fn s] (into {} (map (juxt key-fn val-fn) s))))

;; Again
(defmacro fn->       [& forms] `(fn [x#] (-> x# ~@forms)))
(defmacro fn->>      [& forms] `(fn [x#] (->> x# ~@forms)))
(defmacro fn-some->  [& forms] `(fn [x#] (some-> x# ~@forms)))
(defmacro fn-some->> [& forms] `(fn [x#] (some->> x# ~@forms)))

(defn- when-not-pred-fn [p]
  #(when-not (p %)
     %))

(def not-zero (when-not-pred-fn zero?))
(def not-neg  (when-not-pred-fn neg?))

(defn stringy? [x]
  (or (string? x) (keyword? x)))

(defn throw-err [e]
  (when (instance? Throwable e)
    (throw e))
  e)

(defmacro <? [ch]
  `(throw-err (async/<! ~ch)))

(defn <?! [ch]
  (throw-err (async/<!! ch)))

(defmacro go-catching [& body]
  `(async/go
     (try
       ~@body
       (catch Exception e#
         e#))))

(defn close-with! [ch v]
  (when v
    (async/put! ch v))
  (async/close! ch)
  ch)

;; Taken from encore
(defn fq-name "Like `name` but includes namespace in string when present."
  [x]
  (if (string? x) x
      (let [n (name x)]
        (if-let [ns (namespace x)] (str ns "/" n) n))))

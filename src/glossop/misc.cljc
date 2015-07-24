(ns glossop.misc)

#?(:clj
   (do
     (defmacro fn-some->  [& forms] `(fn [x#] (some-> x# ~@forms)))
     (defmacro fn-some->> [& forms] `(fn [x#] (some->> x# ~@forms)))))

(defn- when-not-pred-fn [p]
  #(when-not (p %)
     %))

(def not-zero (when-not-pred-fn zero?))
(def not-neg  (when-not-pred-fn neg?))

(def stringy? (some-fn string? keyword?))

;; Taken from encore
(defn fq-name "Like `name` but includes namespace in string when present."
  [x]
  (if (string? x) x
      (let [n (name x)]
        (if-let [ns (namespace x)] (str ns "/" n) n))))

(ns glossop.macros)

(defmacro go-catching [& body]
  `(cljs.core.async.macros/go
     (try
       ~@body
       (catch js/Error e#
         (.error js/console "Caught exception in (go) block" e# (.stack e#))
         e#))))

(defmacro <? [x]
  `(glossop.core/throw-err (cljs.core.async/<! ~x)))

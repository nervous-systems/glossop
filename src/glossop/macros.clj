(ns glossop.macros)

(defmacro go-catching [& body]
  `(cljs.core.async.macros/go
     (try
       ~@body
       (catch js/Error e#
         e#))))

(defmacro <? [x]
  `(cljs.core.async/<! ~x))

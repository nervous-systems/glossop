(ns glossop.error.macros)

(defmacro go-catching [& body]
  `(cljs.core.async/go
     (try
       ~@body
       (catch js/Error e#
         e#))))

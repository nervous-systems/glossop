(defproject io.nervous/glossop "0.2.1"
  :description "Miscellaneous Clojure/script utilities"
  :url "https://github.com/nervous-systems/glossop"
  :license {:name "Unlicense" :url "http://unlicense.org/UNLICENSE"}
  :scm {:name "git" :url "https://github.com/nervous-systems/glossop"}
  :source-paths ["src"]
  :dependencies
  [[org.clojure/clojure       "1.8.0"]
   [org.clojure/clojurescript "1.8.34"]
   [org.clojure/core.async    "0.2.374"]]
  :clean-targets ["target" "out"]
  :cljsbuild
  {:builds [{:id "glossop"
             :source-paths ["src"]
             :compiler {:output-to     "out/glossop.js"
                        :output-dir    "out"
                        :target        :nodejs
                        :optimizations :none
                        :source-map    true}}]})

(defproject io.nervous/glossop "1.0.0-SNAPSHOT"
  :description "Miscellaneous Clojure utilities"
  :url "https://github.com/nervous-systems/glossop"
  :license {:name "Unlicense" :url "http://unlicense.org/UNLICENSE"}
  :scm {:name "git" :url "https://github.com/nervous-systems/glossop"}
  :deploy-repositories [["clojars" {:creds :gpg}]]
  :signing {:gpg-key "moe@nervous.io"}
  :global-vars {*warn-on-reflection* true}
  :source-paths ["src" "test"]
  :dependencies
  [[org.clojure/clojure    "1.7.0"]
   [org.clojure/core.async "0.1.346.0-17112a-alpha"]
   [org.clojure/clojurescript  "0.0-3308"]]
  :clean-targets ["target" "out"]
  :cljsbuild
  {:builds [{:id "glossop"
             :source-paths ["src"]
             :compiler {:output-to "out/glossop.js"
                        :output-dir "out"
                        :target :nodejs
                        :optimizations :none
                        :source-map true}}]}
  :profiles
  {:dev {:dependencies
         [[com.cemerick/piggieback "0.2.1"]
          [org.clojure/tools.nrepl "0.2.10"]]
         :repl-options {:nrepl-middleware
                        [cemerick.piggieback/wrap-cljs-repl]}}})

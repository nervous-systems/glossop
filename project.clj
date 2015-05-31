(defproject io.nervous/glossop "0.1.0"
  :description "Miscellaneous Clojure utilities"
  :url "https://github.com/nervous-systems/glossop"
  :license {:name "Unlicense" :url "http://unlicense.org/UNLICENSE"}
  :scm {:name "git" :url "https://github.com/nervous-systems/glossop"}
  :deploy-repositories [["clojars" {:creds :gpg}]]
  :signing {:gpg-key "moe@nervous.io"}
  :global-vars {*warn-on-reflection* true}
  :source-paths ["src" "test"]
  :dependencies
  [[org.clojure/clojure    "1.6.0"]
   [org.clojure/core.async "0.1.346.0-17112a-alpha"]]
  :exclusions [[org.clojure/clojure]])

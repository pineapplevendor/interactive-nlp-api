 (defproject interactive-nlp-api "0.1.0-SNAPSHOT"
   :description "FIXME: write description"
   :dependencies [[org.clojure/clojure "1.8.0"]
                  [clj-http "3.9.1"]
                  [ring-cors "0.1.13"]
                  [metosin/compojure-api "1.1.11"]]
   :ring {:handler interactive-nlp-api.handler/app}
   :uberjar-name "server.jar"
   :profiles {:dev {:dependencies [[javax.servlet/javax.servlet-api "3.1.0"]]
                   :plugins [[lein-ring "0.12.0"]]}})

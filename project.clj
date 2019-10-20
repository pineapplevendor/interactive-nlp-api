 (defproject interactive-nlp-api "0.1.0"
   :description "Get subject-relation-object triples of text"
   :dependencies [[org.clojure/clojure "1.10.0"]
                  [metosin/spec-tools "0.10.0"]
                  [clj-http "3.9.1"]
                  [ring-cors "0.1.13"]
                  [ring/ring-mock "0.4.0"]
                  [org.clojure/data.json "0.2.6"]
                  [com.taoensso/faraday "1.9.0" :exclusions [joda-time]]
                  [clojure-stanford-openie "1.0.0" :exclusions [org.slf4j/slf4j-api]] 
                  [amazonica "0.3.147" ]
                  [metosin/compojure-api "1.1.12"]]
   :managed-dependencies [[com.fasterxml.jackson.core/jackson-databind "2.6.7.2"]
                          [org.flatland/ordered "1.5.7"]
                          [com.taoensso/nippy "2.12.0"]
                          [com.amazonaws/aws-java-sdk-core "1.11.659"]
                          [org.apache.httpcomponents/httpcore "4.4.11"]
                          [org.apache.httpcomponents/httpclient "4.5.9"]
                          [com.amazonaws/aws-java-sdk-dynamodb "1.11.659"]]
   :min-lein-version "2.9.1"
   :ring {:handler interactive-nlp-api.handler/app}
   :uberjar-name "interactive-nlp-api-standalone.jar"
   :profiles {:uberjar {:dependencies [[javax.servlet/javax.servlet-api "3.1.0"]]}
              :dev {:dependencies [[javax.servlet/javax.servlet-api "3.1.0"]]
                    :plugins [[lein-ring "0.12.5"]
                              [cider/cider-nrepl "0.21.1"]
                              [lein-exec "0.3.7"]
                              [lein-cljfmt "0.6.4"]]}})

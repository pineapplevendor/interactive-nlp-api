(ns interactive-nlp-api.handler
  (:require [compojure.api.sweet :refer :all]
            [schema.core :as s]
            [ring.util.http-response :refer :all]
            [clojure-stanford-openie.core :as openie]
            [ring.middleware.cors :refer [wrap-cors]]))

(s/defschema UserInput
  {:text s/Str})

(s/defschema Relation
  {:subject s/Str
   :relation s/Str
   :object s/Str})

(s/defschema Relations
  [[Relation]])

(def pipeline (openie/initialize-openie-pipeline))

(def app
  (api
   {:swagger
    {:ui "/"
     :spec "/swagger.json"
     :data {:info {:title "interactive-nlp-api"
                   :description "Get subject-relation-object triples from text"}}}}
   (context "/api" []
     :middleware [#(wrap-cors % :access-control-allow-origin #".*"
                              :access-control-allow-headers ["Origin" "X-Requested-With"
                                                             "Content-Type" "Accept"]
                              :access-control-allow-methods [:options :post])]

     (POST "/get-relations" []
       :summary "get subject-relation-object triples from text"
       :return Relations
       :body [user-input UserInput]
       (ok (s/validate Relations (openie/get-relations pipeline (:text user-input))))))))


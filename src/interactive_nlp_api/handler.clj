(ns interactive-nlp-api.handler
  (:require [compojure.api.sweet :refer :all]
            [ring.util.http-response :refer :all]
            [schema.core :as s]
            [ring.middleware.cors :refer [wrap-cors]]
            [interactive-nlp-api.openie-facade :as openie-facade]
            [interactive-nlp-api.next-question-manager :as next-question-manager]))

(s/defschema UserInput
  {:text s/Str})

(s/defschema Relation
  {:subject s/Str :relation s/Str :object s/Str})

(s/defschema NextQuestion
  {:question-text s/Str})

(s/defschema Relations
  [Relation])

(s/defschema OutputRelations
  {:outputText s/Str})

(def app
  (api
   {:swagger
    {:ui "/"
     :spec "/swagger.json"
     :data {:info {:title "Interactive-nlp-api"
                   :description "Compojure Api example"}
            :tags [{:name "api", :description "apis for collecting information"}]}}}

   (context "/api" []
     :middleware [#(wrap-cors % :access-control-allow-origin #".*"
                              :access-control-allow-headers ["Origin" "X-Requested-With"
                                                             "Content-Type" "Accept"]
                              :access-control-allow-methods [:get :options :post :delete])]

     :tags ["api"]

     (POST "/get-relations" []
       :return Relations
       :body [userInput UserInput]
       :summary "extract relations from text"
       (ok (map #(s/validate Relation %)
                (openie-facade/get-relations (:text userInput)))))

     (POST "/get-next-question" []
       :return NextQuestion
       :body [userInput UserInput]
       :summary "get next question based on text"
       (ok (s/validate NextQuestion
                       (next-question-manager/get-next-question
                        (openie-facade/get-relations (:text userInput)))))))))


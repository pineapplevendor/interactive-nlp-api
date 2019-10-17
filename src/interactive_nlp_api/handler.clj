(ns interactive-nlp-api.handler
  (:require [compojure.api.sweet :refer :all]
            [ring.util.http-response :refer :all]
            [clojure-stanford-openie.core :as openie]
            [clojure.spec.alpha :as s]
            [spec-tools.spec :as spec]
            [ring.middleware.cors :refer [wrap-cors]]))

(s/def ::user-input spec/string?)
(s/def ::relation (s/and :subject spec/string?
                         :relation spec/string?
                         :object spec/string?))
(s/def ::sentence-relations (s/and :sentence spec/string?
                                   :relations (s/coll-of ::relation)))

(s/def ::sentences-to-relations (s/coll-of ::sentence-relations))

(def pipeline (openie/initialize-openie-pipeline))

(def app
  (api
   {:coercion :spec}

   (GET "/" []
     "Home")

   (context "/api" []
     :middleware [#(wrap-cors % :access-control-allow-origin #".*"
                              :access-control-allow-headers ["Origin" "X-Requested-With"
                                                             "Content-Type" "Accept"]
                              :access-control-allow-methods [:options :post])]
     (POST "/get-relations" []
       :summary "get subject-relation-object triples from text"
       :return ::sentences-to-relations
       :body [user-input ::user-input]
       (ok (openie/get-relations pipeline (:text user-input)))))))


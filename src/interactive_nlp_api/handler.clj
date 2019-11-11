(ns interactive-nlp-api.handler
  (:require [compojure.api.sweet :refer :all]
            [ring.util.http-response :refer :all]
            [clojure.spec.alpha :as s]
            [interactive-nlp-api.relations-manager :as relations-manager]
            [interactive-nlp-api.benchmark-manager :as benchmark-manager]
            [spec-tools.spec :as spec]
            [ring.middleware.cors :refer [wrap-cors]]))

(s/def ::user-input spec/string?)
(s/def ::relation (s/and :subject spec/string?
                         :relation spec/string?
                         :object spec/string?))
(s/def ::sentence-relations (s/and :sentence spec/string?
                                   :relations (s/coll-of ::relation)))
(s/def ::sentences-to-relations (s/coll-of ::sentence-relations))

(s/def ::experiment-id spec/string?)
(s/def ::benchmark-entry-id spec/string?)
(s/def ::benchmark-entry (s/and :sentence spec/string?
                                :sentence-id spec/string?
                                :next-sentence-id spec/string?))
(s/def ::re-written-sentence spec/string?)

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
       (ok (relations-manager/get-relations (:text user-input))))
     
     (GET "/get-original-sentence/:benchmark-entry-id" []
       :path-params [benchmark-entry-id :- ::benchmark-entry-id]
       :summary "Retrieve the benchmark-entry identified by the given benchmark-entry-id"
       :return ::benchmark-entry
       (ok (benchmark-manager/get-benchmark-entry benchmark-entry-id)))

     (GET "/get-next-sentence-in-experiment/:experiment-id" []
       :path-params [experiment-id :- ::experiment-id]
       :summary "Get the next benchmark entry in an experiment to re-write"
       :return ::benchmark-entry-id
       (ok (benchmark-manager/get-next-sentence-in-experiment experiment-id)))

     (POST "/submit-sentence/:experiment-id/:benchmark-entry-id" []
       :path-params [experiment-id :- ::experiment-id]
       :summary "Submit a re-written sentence for a benchmark entry"
       :body [re-written-benchmark-entry :- ::benchmark-entry]
       (ok (benchmark-manager/submit-sentence experiment-id re-written-benchmark-entry))))))


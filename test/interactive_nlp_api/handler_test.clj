(ns interactive-nlp-api.handler-test
  (:require [clojure.test :refer :all]
            [cheshire.core :as cheshire]
            [ring.mock.request :as mock]
            [interactive-nlp-api.handler :refer :all]))

(defn parse-body [body]
  (cheshire/parse-string (slurp body) true))

(deftest verify-retrieving-relations
  (let [response (app (-> (mock/request :post "/api/get-relations")
                          (mock/content-type "application/json")
                          (mock/body (cheshire/generate-string {:text "I drink coke and water."}))))
        body (parse-body (:body response))]
    (is (= (:status response) 200))
    (is (= body (list {:sentence "I drink coke and water."
                       :relations [{:subject "I" :relation "drink" :object "water"}
                                   {:subject "I" :relation "drink" :object "coke"}]})))))


(ns interactive-nlp-api.openie-facade
  (:require [clj-http.client :as http-client])
  (:import ))

(def openie-base-url "http://localhost:9000/")
(def openie-query-params "?properties={%22annotators%22%3A%22openie%22%2C%22outputFormat%22%3A%22json%22}")

(def openie-url (str openie-base-url openie-query-params))

(defn call-openie 
    [text]
    (http-client/post openie-url {:body text :as :auto}))

(defn get-relations-from-sentence
    [sentence]
    (map #(select-keys % [:subject :relation :object]) (:openie sentence)))

(defn get-relations
    [text]
    (reduce concat 
        (map get-relations-from-sentence 
            (:sentences 
                (:body 
                    (call-openie text))))))


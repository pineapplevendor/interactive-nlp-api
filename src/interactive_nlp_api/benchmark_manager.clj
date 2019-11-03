(ns interactive-nlp-api.benchmark-manager
  (:require [amazonica.aws.dynamodbv2 :as db]))

(def benchmark-table "original-sentences")

(defn get-benchmark-entry [benchmark-entry-id]
  (db/get-item :table-name benchmark-table
               :key {:sentence-id benchmark-entry-id}))


(ns interactive-nlp-api.benchmark-manager
  (:require [amazonica.aws.dynamodbv2 :as db]))

(def benchmark-table "original-sentences")
(def next-entry-table "next-entries")

(defn- get-benchmark-sentence [benchmark-sentence-id]
  (:item (db/get-item :table-name benchmark-table
               :key {:sentence-id benchmark-sentence-id})))

(defn- get-next-benchmark-sentence-id [experiment-id]
  (:item (db/get-item :table-name next-entry-table
                      :key {:experiment-id experiment-id})))

(defn get-next-sentence-in-experiment [experiment-id]
  (get-benchmark-sentence (:next-entry-id (get-next-benchmark-sentence-id experiment-id))))

(defn submit-sentence [experiment-id re-written-benchmark-entry]
  (db/put-item :table-name experiment-id
               :item re-written-benchmark-entry)
  (cond (= (:sentence-id re-written-benchmark-entry)
           (:sentence-id (get-next-sentence-in-experiment experiment-id)))
        (db/put-item :table-name next-entry-table
                     :item {:experiment-id experiment-id
                            :next-entry-id (:next-sentence-id re-written-benchmark-entry)})))

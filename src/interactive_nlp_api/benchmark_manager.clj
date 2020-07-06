(ns interactive-nlp-api.benchmark-manager
  (:require [interactive-nlp-api.relations-manager :as relations-manager]
            [amazonica.aws.dynamodbv2 :as db]))

(def benchmark-table "original-sentences")
(def next-entry-table "next-entries")

(def evaluations-table "rewrite-evaluations")
(def next-evaluations-table "next-evaluations")

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

(defn- get-next-evaluation-in-experiment [experiment-id]
  (:item (db/get-item :table-name next-evaluations-table
                      :key {:experiment-id experiment-id})))

(defn- get-rewritten-sentence [experiment-id sentence-id]
  (:item (db/get-item :table-name experiment-id
                      :key {:sentence-id sentence-id})))

(defn get-next-relations-to-evaluate [experiment-id]
  (let [next-evaluation-sentence-id (:next-evaluation-id (get-next-evaluation-in-experiment experiment-id))
        benchmark-sentence (:sentence (get-benchmark-sentence next-evaluation-sentence-id))
        rewritten-sentence (:sentence (get-rewritten-sentence experiment-id next-evaluation-sentence-id))]
    {:original-sentence benchmark-sentence
     :original-relations (relations-manager/get-relations benchmark-sentence)
     :rewritten-relations (relations-manager/get-relations rewritten-sentence)}))

(defn submit-evaluation [experiment-id is-rewrite-preferred]
  (let [next-evaluation-sentence-id (:next-evaluation-id (get-next-evaluation-in-experiment experiment-id))
        evaluated-sentence (get-benchmark-sentence next-evaluation-sentence-id)]
    (db/put-item :table-name evaluations-table
                 :item {:sentence-id (:sentence-id evaluated-sentence)
                        :evaluation is-rewrite-preferred})
    (db/put-item :table-name next-evaluations-table
                 :item {:experiment-id experiment-id
                        :next-evaluation-id (:next-sentence-id evaluated-sentence)})))


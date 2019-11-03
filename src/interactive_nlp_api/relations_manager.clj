(ns interactive-nlp-api.relations-manager
  (:require [clojure-stanford-openie.core :as openie]))

(def pipeline (openie/initialize-openie-pipeline))

(defn get-relations [text]
  (openie/get-relations pipeline text))

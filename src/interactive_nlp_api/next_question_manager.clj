(ns interactive-nlp-api.next-question-manager
  (:require [interactive-nlp-api.little-caesars-facade :as little-caesars-facade]))

(defn get-next-question
  [relations]
  (little-caesars-facade/get-next-question relations))


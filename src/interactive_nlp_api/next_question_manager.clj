(ns interactive-nlp-api.next-question-manager
  (:require [clojure.set :as set]))

(def like-pizza? {:relations #{{:subject "I" :relation "love" :object "pizza"}
                               {:subject "I" :relation "like" :object "pizza"}
                               {:subject "I" :relation "want" :object "pizza"}}
                  :mutual-information 0.8
                  :question-text "Do you like pizza?"})

(def want-cheap? {:relations #{{:subject "I" :relation "am saving" :object "money"}
                               {:subject "I" :relation "save" :object "money"}
                               {:subject "I" :relation "want" :object "cheap food"}}
                  :mutual-information 0.75
                  :question-text "Do you want cheap food?"})

(def near-main? {:relations #{{:subject "I" :relation "live on" :object "main street"}
                              {:subject "I" :relation "live near" :object "main street"}
                              {:subject "I" :relation "am near" :object "main street"}}
                 :mutual-information 0.6
                 :question-text "Are you near Main Street?"})

(def like-wings? {:relations #{{:subject "I" :relation "love" :object "wings"}
                               {:subject "I" :relation "like" :object "wings"}
                               {:subject "I" :relation "want" :object "wings"}
                               {:subject "I" :relation "love" :object "chicken"}
                               {:subject "I" :relation "like" :object "chicken"}
                               {:subject "I" :relation "want" :object "chicken"}
                               {:subject "I" :relation "love" :object "chicken wings"}
                               {:subject "I" :relation "like" :object "chicken wings"}
                               {:subject "I" :relation "want" :object "chicken wings"}}
                  :mutual-information 0.65
                  :question-text "Do you like chicken wings?"})

(def features [like-pizza? want-cheap? near-main? like-wings?])

(defn question-answered?
  [relations question]
  (not-empty
   (set/intersection (:relations question) (set relations))))

(defn get-unanswered-questions
  [questions relations]
  (remove #(question-answered? relations %) questions))

(defn get-best-question
  [questions]
  (if (not-empty questions)
    (do (apply max-key :mutual-information questions))
    (do {:question-text "No more questions"})))

(defn get-best-unanswered-question
  [questions relations]
  (get-best-question (get-unanswered-questions questions relations)))

(defn get-next-question
  [relations]
  (select-keys (get-best-unanswered-question features relations) [:question-text]))


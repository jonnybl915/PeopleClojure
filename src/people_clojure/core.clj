 (ns people-clojure.core
  (:require [clojure.string :as str]) ;this is our import statement
  (:gen-class))                         ;;comments are written after semicolons!!!

(defn -main []
  (let [people (slurp "people.csv")
        people (str/split-lines people)
        people (map (fn [line] (str/split line #","))
                 people)
        header (first people)
        people (rest people)
        people (map (fn [line] (zipmap header line))
                 people)
        people (filter (fn [line] (= "Brazil" (get line "country")))
                 people)]
    people))


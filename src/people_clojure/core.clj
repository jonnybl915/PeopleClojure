 (ns people-clojure.core
  (:require [clojure.string :as str]
            [compojure.core :as c]
            [ring.adapter.jetty :as j]
            [hiccup.core :as h]) ;this is our import statement
  (:gen-class))                         ;;comments are written after semicolons!!!

(defn read-people []
  (let [people (slurp "people.csv")
        people (str/split-lines people)
        people (map (fn [line] (str/split line #","))
                 people)
        header (first people)
        people (rest people)
        people (map (fn [line] (zipmap header line))
                 people)]
    people))

(defn people-html [people]
  [:html
   [:body
    [:ol
     (map (fn [person]
            [:li (str (get person "first_name") " " (get person "last_name"))])
      people)]]])

(defn filter-by-country [people country]
  (filter (fn [person]
            (= country (get person "country")))
    people))

(c/defroutes app
  (c/GET "/:country{.*}" [country]  ;{.*} says 0-howeverMany chars allowed 
    (let [people (read-people)
          people (if (= 0 (count country))
                   people
                   (filter-by-country people country))]
      (h/html (people-html people)))))

(defonce server (atom nil)) ;defining an atom which acts as a "global" variable that we can "mutate"
;changed def -> defonce to ensure it doesn't get overwritten we we restart
(defn -main []
  (if @server ;null check for the server atom to make sure we are actually stopping something
    (.stop @server)) ;stops the server so that we can rerun when we call main again
  (reset! server (j/run-jetty app {:port 3000 :join? false}))) ; ":join?" is telling the program to look for continued input, the hashmap is for configuration


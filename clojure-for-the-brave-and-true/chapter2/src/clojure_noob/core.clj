(ns clojure-noob.core
  (:gen-class))

(defn -main
  [& args]
  (println "I'm a little teapot!")
  (numbers))

(println "Cleanliness is next to godliness")

(defn train
  []
  (println "Choo choo!"))

(defn numbers
  []
  (map inc [1 2 3 4]))

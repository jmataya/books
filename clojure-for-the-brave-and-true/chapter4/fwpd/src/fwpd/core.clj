(ns fwpd.core)
(def filename "suspects.csv")

(def vamp-keys [:name :glitter-index])

(defn str->int
  [str]
  (Integer. str))

(def conversions {:name identity
                  :glitter-index str->int})

(defn convert
  [vamp-key value]
  ((get conversions vamp-key) value))

(defn parse
  "Convert a CSV into rows of columns"
  [string]
  (map #(clojure.string/split % #",")
       (clojure.string/split string #"\n")))

(defn mapify
  "Return a seq of maps like {:name \"Edward Cullen\" :glitter-index 10"
  [rows]
  (map (fn [unmapped-row]
         (reduce (fn [row-map [vamp-key value]]
                   (assoc row-map vamp-key (convert vamp-key value)))
                 {}
                 (map vector vamp-keys unmapped-row)))
       rows))

(defn glitter-filter
  [minimum-glitter records]
  (filter #(>= (:glitter-index %) minimum-glitter) records))

;;
;; Exercise 1: Turn the result of your glitter filter into a list of names.
;;

(defn glitter-filter-names
  [minimum-glitter records]
  (map #(:name %) (glitter-filter minimum-glitter records)))

;;
;; Exercise 2: Write a function, `append`, which will append a new suspect to
;;             your list of suspects.
;;

(defn append
  [existing-suspects new-suspect]
  (conj existing-suspects new-suspect))

;;
;; Exercise 3: Write a function, `validate`, which will check that `:name` and
;;             `:glitter-index` are present when you append. The `validate`
;;             function should accept two arguments: a map of keywords to
;;             validating functions, similar to `conversions`, and the record
;;             to be validated.
;;

(def validators {:name #(get % :name)
                 :glitter-index #(get % :glitter-index)})

(defn validate
  [validators suspect]
  (reduce (fn [is-valid [key validator]]
            (and is-valid (validator suspect)))
          true
          validators))

(defn validated-append
  [existing-suspects new-suspect]
  (if (validate validators new-suspect)
    (conj existing-suspects new-suspect)
    existing-suspects))

;;
;; Exercise 4: Write a function that will take your list of maps and convert it
;;             back to a CSV string. You'll need to use the `clojure.string/join`
;;             function.
;;

(defn create-csv
  [suspects]
  (clojure.string/join "\n" (map (fn [suspect]
                                   (clojure.string/join "," (map #(second %) suspect)))
                                 suspects)))

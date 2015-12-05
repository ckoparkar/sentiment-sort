(ns sentiment-sort.core
  (:require [clojure.tools.cli :refer [parse-opts]]
            [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [clojure.string :as str])
  (:gen-class))

(defn abs
  "(abs n) is the absolute value of n"
  [n]
  (if (neg? n)
    (- n)
    n))

;; real meat of the whole thing
(defn s-sort
  "Sorts xs based on sentiment"
  ([xs]
   (sort-by abs > xs))
  ([n xs]
   (take n (s-sort xs))))

(defn parse-csv
  [path]
  (loop [[line & lines] (with-open [in-file (io/reader path)]
                          (doall (csv/read-csv in-file)))
         acc []]
    (if (nil? line)
      acc
      (recur lines
             (conj acc {:n (Integer/parseInt (first line))
                        :xs (map #(Integer/parseInt %) (str/split (first (rest line)) #",") )}
                   )))))

(defn s-sort-csv
  ([xs] (s-sort-csv xs []))
  ([[x & xs] acc]
   (if (nil? x)
     acc
     (s-sort-csv xs (conj acc [(:n x) (:xs x) (s-sort (:n x) (:xs x))])))))

(def cli-options
  [["-n" "--number N" "Take n from sorted array"
    :id :n
    :default 10
    :parse-fn #(Integer/parseInt %)]

   ["-a" "--array xs" "Array of numbers to sort"
    :id :xs
    :default '("-1" "-2" "-3" "-4" "-5")
    :parse-fn #(str/split % #" ")]

   ["-csv-in" "--csv-in PATH" "Path of csv file"
    :id :csv-file]

   ["-h" "--help"]])

(defn exit [status msg]
  (println msg)
  (System/exit status))

(defn usage [options-summary]
  (str/join "\n"
   ["Usage: sentiment-sort [options]"
    ""
    "Options:"
    options-summary]))

;; TODO(cskksc): avoid extra map. get everything done in cli-options.
(defn -main
  [& args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)
        n (:n options)
        xs (:xs options)
        csv-file (:csv-file options)]
    (cond
      (:help options) (exit 0 (usage summary))
      csv-file (spit "out.csv" (str/join "\n" (s-sort-csv (parse-csv csv-file))))
      :else (println "Running for n:" n ",xs:" xs "\n =" (s-sort n (map #(Integer/parseInt %) xs))))))

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

(defn read-csv
  [path]
  (with-open [in-file (io/reader path)]
    (doall (csv/read-csv in-file))))

(defn parse-csv
  [path]
  (for [line (read-csv path)
        :let [toTake (Integer. (first line))
              strs (str/split (second line) #",")
              numbers (map #(Integer. %) strs)]]
    {:take toTake :numbers numbers}))

(defn s-sort-csv
  [csv-map]
  (for [line csv-map
        :let [toTake (:take line)
              numbers (:numbers line)
              sorted (s-sort toTake numbers)]]
    [toTake numbers sorted]))

(def cli-options
  [["-n" "--take N" "Take n from sorted array"
    :id :take
    :default 10
    :parse-fn #(Integer. %)]

   ["-nums" "--numbers xs" "Array of numbers to sort"
    :id :numbers
    :default '("-1" "-2" "-3" "-4" "-5")
    :parse-fn #(str/split % #",")]

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
        toTake (:take options)
        numbers (:numbers options)
        csv-file (:csv-file options)]
    (cond
      (:help options) (exit 0 (usage summary))
      csv-file (spit "out.csv" (str/join "\n" (s-sort-csv (parse-csv csv-file))))
      :else (println "Running for take:" toTake ",numbers:" numbers "\n ="
                     (s-sort toTake (map #(Integer. %) numbers))))))

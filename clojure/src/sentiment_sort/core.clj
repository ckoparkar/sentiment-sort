(ns sentiment-sort.core
  (:require [clojure.tools.cli :refer [parse-opts]]
            [clojure.data.csv :as csv]
            [clojure.java.io :as io])
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
                        :xs (map #(Integer/parseInt %) (clojure.string/split (first (rest line)) #",") )}
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
    :parse-fn #(clojure.string/split % #" ")]

   ["-csv" "--csv PATH" "Path of csv file"
    :id :csv-file]])

;; TODO(cskksc): avoid extra map. get everything done in cli-options.
(defn -main
  [& args]
  (let [opts (parse-opts args cli-options)
        n (get-in opts [:options :n])
        xs (get-in opts [:options :xs])
        csv-file (get-in opts [:options :csv-file])]
    (if csv-file
      (spit "out.csv" (clojure.string/join "\n" (s-sort-csv (parse-csv csv-file))))
      (println (s-sort n (map #(Integer/parseInt %) xs)))
      )))

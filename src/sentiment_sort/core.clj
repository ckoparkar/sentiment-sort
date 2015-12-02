(ns sentiment-sort.core
  (:require [clojure.tools.cli :refer [parse-opts]])
  (:gen-class))

(defn abs
  "(abs n) is the absolute value of n"
  [n]
  (if (neg? n)
    (- n)
    n))

(defn s-sort
  "Sorts xs based on sentiment"
  ([xs]
   (sort-by abs > xs))
  ([n xs]
   (take n (s-sort xs))))

(def cli-options
  [["-n" "--number N" "Take n from sorted array"
    :id :n
    :default 10
    :parse-fn #(Integer/parseInt %)]

   ["-a" "--array xs" "Array of numbers to sort"
    :id :xs
    :default '(-2 1 -5 3 4 -4 -1 -2 -3 5 -1 4 2)
    :parse-fn #(clojure.string/split % #" ")]])

;; TODO(cskksc): avoid extra map. get everything done in cli-options.
(defn -main
  [& args]
  (let [opts (parse-opts args cli-options)
        n (get-in opts [:options :n])
        xs (get-in opts [:options :xs])]
    (println (s-sort n (map #(Integer/parseInt %) xs)))))

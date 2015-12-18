(ns sentiment-sort.core
  (:require [clojure.tools.cli :refer [parse-opts]]
            [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [clojure.string :as str])
  (:gen-class))

(defn generate-sort-fn
  "Take a sort preference, (+ve, -ve, none) and returns a sort fn"
  [preference]
  (fn [x y]
    (let [a (Math/abs x) b (Math/abs y)]
      (cond
        (or (not= a b) (str/blank? preference)) (> a b)
        (= preference "+") (> x y)
        :else (< x y)
        ))))

;; real meat of the whole thing
(defn s-sort
  "Sorts xs based on sentiment"
  ([xs]
   (sort (generate-sort-fn "") xs))
  ([xs preference]
   (sort (generate-sort-fn preference) xs))
  ([n xs preference]
   (take n (s-sort xs preference))))

(defn s-sort-csv
  "Sort csv maps.
  Example:

  in  => [{:take 2 :numbers '(3 4 5)} {:take 3 :numbers '(1 2 3)}]
  out => ([2 '(3 4 5) '(5 4)]
  ,       [3 '(1 2 3) '(3 2 1)])"
  [preference csv-map]
  (for [line csv-map
        :let [to-take (:take line)
              numbers (:numbers line)
              sorted (s-sort to-take numbers preference)]]
    [to-take numbers sorted]))

(defn read-csv
  "Return contents of csv file into a vector."
  [path]
  (with-open [in-file (io/reader path)]
    (doall (csv/read-csv in-file))))

(defn parse-csv
  "Parses csv file into array of maps.
  Example:

  in => array of csv lines
  2,\"-1,-2,-3,-5,-4\"
  3,\"-3,-5,-4\"
  4,\"-1,\n\n-2\n,-3\n\n,\n-4\n\n,-5,-6\"
  out => [{:take 2, :numbers (-1 -2 -3 -5 -4)}
  ,       {:take 3, :numbers (-3 -5 -4)}
  ,       {:take 2, :numbers (-6 -5 -4 -3 -2 -1)}]"
  [lines]
  (for [line lines
        :let [to-take (-> (first line)
                         (str/replace "\n" "")
                         Integer.)
              strs (-> (second line)
                      (str/replace "\n" "")
                      (str/split #","))
              numbers (map #(Integer. %) strs)]]
    {:take to-take :numbers numbers}))

(def cli-options
  [["-n" "--take N" "Take N after sorting."
    :id :take
    :default 10
    :parse-fn #(Integer. %)]

   ["-nums" "--numbers xs" "Array of numbers to sort"
    :id :numbers
    :default '("-1" "-2" "-3" "-4" "-5")
    :parse-fn #(str/split % #",")]

   ["-csv-in" "--csv-in PATH" "Path of csv file"
    :id :csv-file]

   ["-pref" "--prefer x" "If blank, maintain initial order. If + or -, give it higher preference."
    :id :sort-preference
    :default ""]

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
        to-take (:take options)
        numbers (:numbers options)
        csv-file (:csv-file options)
        preference (:sort-preference options)]
    (cond
      (:help options) (exit 0 (usage summary))
      csv-file (->> (read-csv csv-file)
                  parse-csv
                  (s-sort-csv preference)
                  (str/join "\n")
                  (spit "out.csv"))
      :else (println "Running for take:" to-take ",numbers:" numbers "\n ="
                     (s-sort to-take (map #(Integer. %) numbers) preference)))))

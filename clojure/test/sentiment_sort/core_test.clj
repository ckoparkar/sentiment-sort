(ns sentiment-sort.core-test
  (:require [clojure.test :refer :all]
            [sentiment-sort.core :refer :all]))

(deftest s-sort-test
  (testing "(s-sort xs)"
    (is (= '(-3 -2 -1) (s-sort '(-1 -2 -3))))
    (is (= '(-5 -4 4 3 2 -1) (s-sort '(3 -4 -1 2 4 -5))))
    (is (= '(-5 4 -4 3 -2 1 -1) (s-sort '(-2 1 -5 3 4 -4 -1))))
    (is (= '(-5 4 -3 2 -1) (s-sort '(-1 2 -3 4 -5))))
    )
  (testing "(s-sort xs prefrence)"
    (is (= '(-5 4 -4 3 -3 2 -1) (s-sort '(3 -4 -1 2 4 -5 -3) "+")))
    (is (= '(-5 -4 4 -3 3 2 -1) (s-sort '(3 -4 -1 2 4 -5 -3) "-")))
    (is (= '(-5 -4 4 3 -3 2 -1) (s-sort '(3 -4 -1 2 4 -5 -3) "")))
    )
  (testing "(s-sort n xs preference)"
    (is (= '(-5 4) (s-sort 2 '(3 -4 -1 2 4 -5 -3) "+")))
    (is (= '(-5 -4 4) (s-sort 3 '(3 -4 -1 2 4 -5 -3) "-")))
    (is (= '(-5) (s-sort 1 '(3 -4 -1 2 4 -5 -3) "")))
    ))

(deftest parse-csv-test
  (testing "parse-csv"
    (is (= (parse-csv '(["2" "-1,-2,-3,-5,-4"]
                        ["\n\n3\n\n" "-3,-5,-4"]
                        ["4" "-1,\n\n-2\n,-3\n\n,\n-4\n\n,-5,-6"]))
           [{:take 2 :numbers [-1 -2 -3 -5 -4]}
            {:take 3 :numbers [-3 -5 -4]}
            {:take 4 :numbers [-1 -2 -3 -4 -5 -6]}]))))


(deftest s-sort-csv-test
  (testing "s-sort-csv"
    (is (= (s-sort-csv "" [{:take 2 :numbers [-1 -2 -3 -5 -4]}])
           '([2 [-1 -2 -3 -5 -4] [-5 -4]])
           ))))

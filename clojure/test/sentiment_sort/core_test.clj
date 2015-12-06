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

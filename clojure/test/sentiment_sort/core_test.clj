(ns sentiment-sort.core-test
  (:require [clojure.test :refer :all]
            [sentiment-sort.core :refer :all]))

(deftest s-sort-test
  (testing "We sort correctly"
    (is (= '(-5 4) (s-sort 2 '(-2 1 -5 3 4 -4 -1))))
    (is (= '(-5 4 -4) (s-sort 3 '(-2 1 -5 3 4 -4 -1))))
    ))

(ns sentiment-sort.core)

"Design an efficient program that picks the first N among a sorted array of numbers representing customer ratings from -5 to 5 based on sentiment . Positive or Negative Sentiments are ranked higher than median/unbiased ones. eg: -4 should be sorted higher than 3; 2 sorted higher than -1; 5 is sorted higher than 4, and -5 is sorted higher than -4, etc. The array may be of any size < 100, will be un-sorted, and you need to pick only N. You can use any language you like."

(defn abs
  "(abs n) is the absolute value of n"
  [n]
  (if (neg? n)
    (- n)
    n))

(defn s-sort
  "Sorts xs based on sentiment"
  [n xs]
  (take n (sort-by abs > xs)))

(s-sort 2 '(-2 1 -5 3 4 -4 -1))

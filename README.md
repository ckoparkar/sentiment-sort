# sentiment-sort

A program that picks the first N among a sorted array of numbers representing customer ratings from -5 to 5 based on sentiment . Positive or Negative Sentiments are ranked higher than median/unbiased ones. eg: -4 should be sorted higher than 3; 2 sorted higher than -1; 5 is sorted higher than 4, and -5 is sorted higher than -4, etc.

### Clojure

```
% java -jar bin/sentiment-sort-0.1.0-standalone.jar -h
Usage: sentiment-sort [options]

Options:
  -n, --take N            10                          Take n from sorted array
  -nums, --numbers xs     ("-1" "-2" "-3" "-4" "-5")  Array of numbers to sort
  -csv-in, --csv-in PATH                              Path of csv file
  -h, --help
```

Examples:

1.

	$ java -jar bin/sentiment-sort-0.1.0-standalone.jar --csv-in ../input.csv
	$ cat out.csv


Running time:

```2.63s user 0.10s system 217% cpu 1.252 total```

2.

	$ java -jar bin/sentiment-sort-0.1.0-standalone.jar --take 5 --numbers 1,2,3,4,5

### Go

```
% bin/go-sentiment-osx -h
Usage of go-sentiment:
  -a value
		comma seperated ints to sort
  -csv-in FILE
		Sorts each line of FILE.
  -n N
		Take N after sorting. (default 10)
```

Examples:

1.

	$ bin/go-sentiment-osx -csv-in ../input.csv
	$ cat out.csv

Running time:

```0.00s user 0.00s system 86% cpu 0.009 total```

2.

	$ bin/go-sentiment-osx -n 2 -a 1,2,3,4,5

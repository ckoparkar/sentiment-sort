package main

import (
	"encoding/csv"
	"flag"
	"fmt"
	"log"
	"math"
	"os"
	"sort"
	"strconv"
	"strings"
)

var (
	csvInput = flag.String("csv-in", "../input.csv", "Sorts each line of `FILE`.")
)

func main() {
	flag.Parse()
	f, err := os.Open(*csvInput)
	if err != nil {
		log.Fatal(err)
	}
	csvReader := csv.NewReader(f)
	rows, err := csvReader.ReadAll()
	if err != nil {
		log.Fatal(err)
	}
	for _, row := range rows {
		strArray := strings.Split(row[1], ",")
		numbers := make([]int, 0)
		for _, x := range strArray {
			n, _ := strconv.Atoi(x)
			numbers = append(numbers, n)
		}
		sorted := sentimentSort(numbers)
		fmt.Println(sorted)
	}
}

type intArray []int

func (s intArray) Len() int {
	return len(s)
}

func (s intArray) Swap(i, j int) {
	s[i], s[j] = s[j], s[i]
}

func (s intArray) Less(i, j int) bool {
	return math.Abs(float64(s[i])) > math.Abs(float64(s[j]))
}

func sentimentSort(xs []int) []int {
	sort.Sort(intArray(xs))
	return xs
}

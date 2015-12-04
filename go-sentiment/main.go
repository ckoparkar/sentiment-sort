package main

import (
	"encoding/csv"
	"flag"
	"log"
	"math"
	"os"
	"sort"
	"strconv"
	"strings"
)

var (
	csvInput = flag.String("csv-in", "", "Sorts each line of `FILE`.")
)

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

func main() {
	flag.Parse()
	if *csvInput != "" {
		f, err := os.Open(*csvInput)
		if err != nil {
			log.Fatal(err)
		}
		defer f.Close()
		of, err := os.Create("out.csv")
		var outCsv *csv.Writer
		if err != nil {
			outCsv = csv.NewWriter(os.Stdout)
		}
		outCsv = csv.NewWriter(of)
		csvReader := csv.NewReader(f)
		rows, err := csvReader.ReadAll()
		if err != nil {
			log.Fatal(err)
		}
		for _, row := range rows {
			toTake, _ := strconv.Atoi(row[0])
			strArray := strings.Split(row[1], ",")
			numbers := make([]int, 0)
			for _, x := range strArray {
				n, _ := strconv.Atoi(x)
				numbers = append(numbers, n)
			}
			sorted := sentimentSort(numbers)
			sortedString := ""
			for _, n := range sorted[0:toTake] {
				sortedString += strconv.Itoa(n) + ","
			}
			row = append(row, sortedString[0:len(sortedString)-1])
			outCsv.Write(row)
		}
		outCsv.Flush()
	}
}

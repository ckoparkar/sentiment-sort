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
	csvInput = flag.String("csv-in", "", "Sorts each line of `FILE`.")

	n = flag.Int("n", 10, "Take `N` after sorting.")

	intsFlag intArray
)

type intArray []int

func (a intArray) Len() int {
	return len(a)
}

func (a intArray) Swap(i, j int) {
	a[i], a[j] = a[j], a[i]
}

func (a intArray) Less(i, j int) bool {
	return math.Abs(float64(a[i])) > math.Abs(float64(a[j]))
}

// String converts [1,2,3] to "1,2,3"
func (a *intArray) String() string {
	if len(*a) == 0 {
		return ""
	}
	s := ""
	for _, n := range *a {
		s += strconv.Itoa(n) + ","
	}
	return s[0 : len(s)-1]
}

func (a *intArray) Set(value string) error {
	for _, s := range strings.Split(value, ",") {
		n, err := strconv.Atoi(s)
		if err != nil {
			return err
		}
		*a = append(*a, n)
	}
	return nil
}

func toInts(strs []string) []int {
	numbers := make([]int, 0)
	for _, x := range strs {
		n, _ := strconv.Atoi(x)
		numbers = append(numbers, n)
	}
	return numbers
}

// sentimentSort sorts numbers based on sentiment
// Extremes get priority
func sentimentSort(xs []int) []int {
	sort.Sort(intArray(xs))
	return xs
}

func init() {
	flag.Var(&intsFlag, "a", "comma seperated ints to sort")
}

func main() {
	flag.Parse()
	if len(intsFlag) != 0 {
		if *n > len(intsFlag) {
			*n = len(intsFlag)
		}
		sorted := sentimentSort(intsFlag)
		fmt.Println(sorted[0:*n])
	}
	if *csvInput != "" {
		// init csv reader
		f, err := os.Open(*csvInput)
		if err != nil {
			log.Fatal(err)
		}
		defer f.Close()
		csvReader := csv.NewReader(f)

		// init csv writer
		var csvWriter *csv.Writer
		of, err := os.Create("out.csv")
		defer of.Close()
		if err != nil {
			log.Printf("Couldn't open file, %#v. Writing to Stdout", err)
			csvWriter = csv.NewWriter(os.Stdout)
		}
		csvWriter = csv.NewWriter(of)

		// process each line
		rows, err := csvReader.ReadAll()
		if err != nil {
			log.Fatal(err)
		}
		for _, row := range rows {
			toTake, _ := strconv.Atoi(row[0])
			strs := strings.Split(row[1], ",")
			sorted := sentimentSort(toInts(strs))
			is := intArray(sorted[0:toTake])
			row = append(row, is.String())
			csvWriter.Write(row)
		}
		csvWriter.Flush()
	}
}

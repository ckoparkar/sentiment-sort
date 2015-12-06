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
	csvInput       = flag.String("csv-in", "", "Sorts each line of `FILE`.")
	toTakeFlag     = flag.Int("take", 10, "Take `N` after sorting.")
	sortPreference = flag.String("prefer", "", "If blank, maintain initial order. If + or -, give it higher preference.")

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
	x := math.Abs(float64(a[i]))
	y := math.Abs(float64(a[j]))

	// abs(n) are not equal or we dont care about +ve/-ve
	if x != y || *sortPreference == "" {
		return x > y
	}
	// -ve first
	if *sortPreference == "-" {
		return a[i] < a[j]
	}
	// +ve first
	return a[i] > a[j]
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

func readCSV(path string) ([][]string, error) {
	f, err := os.Open(*csvInput)
	if err != nil {
		return nil, err
	}
	defer f.Close()
	csvReader := csv.NewReader(f)
	rows, err := csvReader.ReadAll()
	if err != nil {
		return nil, err
	}
	return rows, nil
}

func writeCSV(content [][]string) {
	// init csv writer
	var csvWriter *csv.Writer
	of, err := os.Create("out.csv")
	defer of.Close()
	if err != nil {
		log.Printf("Couldn't open file, %#v. Writing to Stdout", err)
		csvWriter = csv.NewWriter(os.Stdout)
	}
	csvWriter = csv.NewWriter(of)
	for _, row := range content {
		csvWriter.Write(row)
	}
	csvWriter.Flush()
}

func processCSV(path string) error {
	rows, err := readCSV(*csvInput)
	if err != nil {
		return err
	}
	content := make([][]string, 0)
	for _, row := range rows {
		toTake, _ := strconv.Atoi(row[0])
		strs := strings.Split(row[1], ",")
		sorted := sentimentSort(toInts(strs))
		is := intArray(sorted[0:toTake])
		row = append(row, is.String())
		content = append(content, row)
	}
	writeCSV(content)
	return nil
}

func main() {
	flag.Var(&intsFlag, "numbers", "comma seperated ints to sort")
	flag.Parse()
	if len(intsFlag) != 0 {
		if *toTakeFlag > len(intsFlag) {
			*toTakeFlag = len(intsFlag)
		}
		sorted := sentimentSort(intsFlag)
		fmt.Println(sorted[0:*toTakeFlag])
	}
	if *csvInput != "" {
		processCSV(*csvInput)
	}
}

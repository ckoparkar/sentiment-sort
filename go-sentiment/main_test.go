package main

import (
	"flag"
	"os"
	"reflect"
	"testing"
)

func TestSentimentSort(t *testing.T) {
	var tests = []struct {
		in  []int
		out []int
	}{
		{[]int{-1, -2, -3}, []int{-3, -2, -1}},
		{[]int{3, -4, -1, 2, 4, -5}, []int{-5, -4, 4, 3, 2, -1}},
	}

	for _, tt := range tests {
		got := sentimentSort(tt.in)
		if !reflect.DeepEqual(tt.out, got) {
			t.Errorf("Expected %v, got %v", tt.out, got)
		}
	}
}

func TestSentimentSortPreferPositive(t *testing.T) {
	os.Args[1] = "-prefer=+"
	flag.Parse()
	var tests = []struct {
		in  []int
		out []int
	}{
		{[]int{-1, -2, -3}, []int{-3, -2, -1}},
		{[]int{3, -4, -1, 2, 4, -5, -3}, []int{-5, 4, -4, 3, -3, 2, -1}},
	}
	for _, tt := range tests {
		got := sentimentSort(tt.in)
		if !reflect.DeepEqual(tt.out, got) {
			t.Errorf("Expected %v, got %v", tt.out, got)
		}
	}
}

func TestSentimentSortPreferNegative(t *testing.T) {
	os.Args[1] = "-prefer=-"
	flag.Parse()
	var tests = []struct {
		in  []int
		out []int
	}{
		{[]int{-1, -2, -3}, []int{-3, -2, -1}},
		{[]int{3, -4, -1, 2, 4, -5, -3}, []int{-5, -4, 4, -3, 3, 2, -1}},
	}
	for _, tt := range tests {
		got := sentimentSort(tt.in)
		if !reflect.DeepEqual(tt.out, got) {
			t.Errorf("Expected %v, got %v", tt.out, got)
		}
	}
}

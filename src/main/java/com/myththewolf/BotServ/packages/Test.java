package com.myththewolf.BotServ.packages;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import org.json.JSONException;

public class Test {
	public static void main(String[] args) throws JSONException, IOException {
		int[] a = new int[100];
		Random r = new Random();
		for (int i = 0; i < 10; i++) {
			int choice = r.nextInt(10);
			a[i] = choice;
		}
		MergeSort(a);
		System.out.println(Arrays.toString(a));
	}

	// Sorting algorithm that takes O(n^2) time to sort an array (n = a.length)
	public static void MergeSort(int[] input) {
		quicksort(input,0,input.length-1);
		
	}

	public static void quicksort(int[] element, int low,int high) {
		int i = low,j = high;
		int pivot = element[low + (high-low)/2];
		while(i <= j) {
			 while (element[i] < pivot) {
	                i++;
	            }
			 while (element[i] > pivot) {
	             	j--;
	            }
			 if(i <= j) {
				 swapArray(element, i, j);
				 i++;
				 j--;
			 }
		}
		if (low < j)
            quicksort(element,low, j);
        if (i < high)
            quicksort(element,i, high);
	}

	public static void mergeSorted(int[] element, int middle, int low, int high) {
		
	}
	public static void swapArray(int[] element, int oldPost, int newPos) {
		int temp = element[newPos];
		element[newPos] = element[oldPost];
		element[oldPost] = temp;
	}
}

package com.github.diegopacheco.sandbox.java.bigonotations;

import java.util.Arrays;

public class BigONotationNLogN {
	
	private static int[] theArray = new int[1000];
	private static int sizeOfArray=0;
	
	public static void main(String[] args) {
		oNBigONotationONlogN(10,60);
		oNBigONotationONlogN(100,60);
		oNBigONotationONlogN(1000,60);
		oNBigONotationONlogN(10000,60);
	}
	
	//
	// O(n log n) -> Quick Search ->  Comparison = Log n!
	//
	private static void oNBigONotationONlogN(int size,int value){
		randomValues(size);
		
		long init = System.currentTimeMillis();
		
		quickSort(0, (size-1) );
		
		System.out.println("Quick Sort in the Array : " + (System.currentTimeMillis()-init)  + " ms" );
		System.out.println(Arrays.toString(theArray));
		sizeOfArray = 0;
	}	
	
	private static void quickSort(int left,int right){
		if (right - left <= 0)
			return;
		
		else {
			int pivot = theArray[right];
			int pivotLocation = partitionArray(left,right,pivot);
			
			quickSort(left, pivotLocation - 1);
			quickSort(pivotLocation + 1, right);
		}
		
	}
	
	private static int partitionArray(int left, int right, int pivot) {
		int leftPointer = left -1;
		int rightPointer = right;
		
		while(true){
			
			while(theArray[++leftPointer] < pivot)
				;
			
			while(rightPointer > 0 && theArray[--rightPointer] > pivot)
				; 
			
			if (leftPointer >= rightPointer){
				break;
			} else{
				swapValues(leftPointer,rightPointer);
			}
		}
		
		swapValues(leftPointer,right);
		return leftPointer;
	}
	
	 public static void swapValues(int indexOne, int indexTwo) {
		 	int temp = theArray[indexOne];
		 	theArray[indexOne] = theArray[indexTwo];
		 	theArray[indexTwo] = temp;
	 }

	private static void randomValues(int n){
		theArray = new int[n];
		for(int i=0; (i+1) < n ;i++){
			sizeOfArray++;
			theArray[i] =  new Double((Math.random() * 1000)).intValue();
		}
	}
	
	
}

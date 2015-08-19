package com.github.diegopacheco.sandbox.java.bigonotations;

import java.util.Arrays;

public class BigONotationOsqareN {
	
	private static int[] theArray = new int[1000];
	private static int sizeOfArray = 0;
	
	public static void main(String[] args) {
		oNBigONotationOsquareN(10);
		oNBigONotationOsquareN(100);
		oNBigONotationOsquareN(1000);
		oNBigONotationOsquareN(10000);
	}
	
	//
	// O(n^2) -> Bouble Sort -> O(n) + O(n) -> 2 loops
	//
	private static void oNBigONotationOsquareN(int size){
		long init = System.currentTimeMillis();
		theArray =  new int[size];
		randomValues(size);
		
		for(int i=sizeOfArray; i > 0 ; i--){
			for(int j = 0; j < i; j++){
				if (theArray[j] > theArray[j+1]){
					int temp = theArray[j];
					theArray[j] = theArray[j+1];
					theArray[j+1] = temp;
				}
			}
		}
		System.out.println("Bouble Sort in the Array : " + (System.currentTimeMillis()-init)  + " ms" );
		System.out.println(Arrays.toString(theArray));
		sizeOfArray = 0;
	}
	
	private static void randomValues(int n){
		for(int i=0; i < (n-1) ;i++){
			sizeOfArray++;
			theArray[i] =  new Double((Math.random() * 1000)).intValue();
		}
	}
	
	
}

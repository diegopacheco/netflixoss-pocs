package com.github.diegopacheco.sandbox.java.bigonotations;


public class BigONotationLogN {
	
	private static int[] theArray = new int[1000];
	private static int sizeOfArray = 0;
	
	
	public static void main(String[] args) {
		oNBigONotationOLogN(100,60);
		oNBigONotationOLogN(1000,60);
		oNBigONotationOLogN(1000,60);
		oNBigONotationOLogN(10000,60);
		oNBigONotationOLogN(100000,60);
	}
	
	//
	// O(log n) -> Binary Search -> as N increases, Log N increase way less.
	//
	private static void oNBigONotationOLogN(int size,int value){
		
		bubleSort(size);
		
		long init = System.currentTimeMillis();
		
		int lowIndex = 0;
		int highIndex = sizeOfArray - 1;
		int timeThrough = 0;
		
		while(lowIndex <= highIndex){
			int middleIndex = (highIndex + lowIndex) / 2;
			if(theArray[middleIndex]<value){
				lowIndex = middleIndex + 1;
			}else if (theArray[middleIndex]>value){
				highIndex = middleIndex -1 ; 
			}else{
				System.out.println("Found the match! " + middleIndex);
				lowIndex = highIndex + 1;
			}
			timeThrough++;
		}
		
		System.out.println("Binary Search in the Array : " + (System.currentTimeMillis()-init)  + " ms" );
		System.out.println("Iterations: " + timeThrough);
		sizeOfArray = 0;
	}	
	
	private static void bubleSort(int size){
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
	}
	
	private static void randomValues(int n){
		for(int i=0; i < (n-1) ;i++){
			sizeOfArray++;
			theArray[i] =  new Double((Math.random() * 1000)).intValue();
		}
	}
	
	
}

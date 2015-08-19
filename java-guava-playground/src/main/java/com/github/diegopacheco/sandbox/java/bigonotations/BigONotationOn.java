package com.github.diegopacheco.sandbox.java.bigonotations;

public class BigONotationOn {
	
	private static int[] theArray = new int[1000];
	private static int sizeOfArray = 0;
	
	public static void main(String[] args) {
		
		oNBigONotation(60,1);
		oNBigONotation(60,100);
		oNBigONotation(60,1000);
		oNBigONotation(60,100000);
		oNBigONotation(60,10000000);
		
	}
	
	//
	// O(n) -> Linear Search -> the operation time will be proporsional of the number of elements of the array.
	//
	private static void oNBigONotation(int value,int size){
		long init = System.currentTimeMillis();
		theArray =  new int[size];
		randomValues(size);
		
		boolean found = false;
		String index = "";
		
		for(int i=0;i < (sizeOfArray-1) ; i++){
			if(theArray[i]==value){
				found = true;
				index += i + " ";
			}
		}
		System.out.println("Result Result: " + found);
		System.out.println("Linear Search in the Array : " + (System.currentTimeMillis()-init)  + " ms" );
		
		sizeOfArray = 0;
	}
	
	private static void randomValues(int n){
		for(int i=0; i < (n-1) ;i++){
			sizeOfArray++;
			theArray[i] =  new Double((Math.random() * 1000)).intValue();
		}
	}
	
}

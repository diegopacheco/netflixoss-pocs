package com.github.diegopacheco.sandbox.java.bigonotations;

public class BigONotationOone {
	
	private static int[] theArray = new int[1000];
	private static int itemsOfArray = 0;
	
	public static void main(String[] args) {
		oOneBigONotation(1,1);
		oOneBigONotation(13,1000);
		oOneBigONotation(60,10000);
	}
	
	//
	// O(1) -> Add One Item to the Array -> No matter the size of the array the operation time is constant.
	//
	private static void oOneBigONotation(int newItem,int size){
		long init = System.currentTimeMillis();
		theArray = new int[size];
		theArray[itemsOfArray++] = newItem;
		System.out.println("Add item Array Took : " + (System.currentTimeMillis()-init)  + " ms" );
	}
	
	
	
}

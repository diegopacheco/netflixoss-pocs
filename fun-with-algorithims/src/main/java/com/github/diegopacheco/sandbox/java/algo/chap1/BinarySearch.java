package com.github.diegopacheco.sandbox.java.algo.chap1;

import java.util.Arrays;

public class BinarySearch {

    private BinarySearch() { }

    /**
     * Searches for the integer key in the sorted array a[].
     * @param key the search key
     * @param a the array of integers, must be sorted in ascending order
     * @return index of key in array a[] if present; -1 if not present
     */
    public static int rank(int key, int[] a) {
        int lo = 0;
        int hi = a.length - 1;
        while (lo <= hi) {
            // Key is in a[lo..hi] or not present.
            int mid = lo + (hi - lo) / 2;
            if      (key < a[mid]) hi = mid - 1;
            else if (key > a[mid]) lo = mid + 1;
            else return mid;
        }
        return -1;
    }

    public static void main(String[] args) {
    	
    	int[] list = new int[]{6,7,8,9,10,5,4};
        int[] whitelist = new int[]{1,2,3,4,5};

        Arrays.sort(whitelist);
        Arrays.sort(list);

        for(int i=0 ; i<  whitelist.length;i++){
            int key = whitelist[i];
            if (rank(key, list) == -1)
                System.out.println(key);
        }
    }
}
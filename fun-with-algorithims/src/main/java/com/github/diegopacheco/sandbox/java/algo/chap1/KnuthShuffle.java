package com.github.diegopacheco.sandbox.java.algo.chap1;

public class KnuthShuffle {
	
	private KnuthShuffle() { }

	public static void shuffle(Object[] a) {
        int N = a.length;
        for (int i = 0; i < N; i++) {
            // choose index uniformly in [i, N-1]
            int r = i + (int) (Math.random() * (N - i));
            Object swap = a[r];
            a[r] = a[i];
            a[i] = swap;
        }
    }

    public static void main(String[] args) {

        // read in the data
        String[] a = new String[]{"2♣", "3♣", "4♣", "5♣", "6♣", "7♣", "8♣", "9♣", "10♣", "J♣", "Q♣", "K♣", "A♣", 
        		  				  "2♦", "3♦", "4♦", "5♦", "6♦", "7♦", "8♦", "9♦", "10♦", "J♦", "Q♦", "K♦", "A♦", 
        		  				  "2♥", "3♥", "4♥", "5♥", "6♥", "7♥", "8♥", "9♥", "10♥", "J♥", "Q♥", "K♥", "A♥", 
        		  				  "2♠", "3♠", "4♠", "5♠", "6♠", "7♠", "8♠", "9♠", "10♠", "J♠", "Q♠", "K♠", "A♠" };

        // shuffle the array
        KnuthShuffle.shuffle(a);

        // print results.
        for (int i = 0; i < a.length; i++)
            System.out.print(a[i]);
    }
}

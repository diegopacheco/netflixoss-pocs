package com.github.diegopacheco.sandbox.java.hash.bitset;

import java.io.UnsupportedEncodingException;
import java.util.BitSet;
import java.util.concurrent.TimeUnit;

public class BitSetMain {
	public static void main(String[] args) {
		long init = System.currentTimeMillis();
		String hash = hashBitSet(java.util.UUID.randomUUID().toString());
		long end = System.currentTimeMillis();
		System.out.println("Hash Took : " + (end-init)  + " ms" );
		System.out.println("Hash: "  + hash);
		
		for(int i=0;i<9;i++){
			init = System.nanoTime();
			hash = hashBitSet(java.util.UUID.randomUUID().toString());
			end = System.nanoTime();
			System.out.println("Hash BitSet Speed: " + (end-init) +  " nano " + TimeUnit.MILLISECONDS.convert((end-init), TimeUnit.NANOSECONDS) + " ms");
			System.out.println("Hash: "  + hash);
		}
	}
	
	private static String hashBitSet(String value){
		try {
			return new Integer(BitSet.valueOf(value.getBytes("UTF-8")).hashCode()).toString();
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}

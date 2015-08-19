package com.github.diegopacheco.sandbox.java.hash.hash64bits;

import java.util.concurrent.TimeUnit;

public class Main {
	public static void main(String[] args) {
		
		long init = System.currentTimeMillis();
		long hash = DataHasher.hash(java.util.UUID.randomUUID().toString());
		long end = System.currentTimeMillis();
		System.out.println("Hash Took : " + (end-init)  + " ms" );
		System.out.println("Hash: "  + hash);
		
		for(int i=0;i<9;i++){
			init = System.nanoTime();
			hash = DataHasher.hash(java.util.UUID.randomUUID().toString());
			end = System.nanoTime();
			System.out.println("Hash 64bits strong hash Speed: " + (end-init) +  " nano " + TimeUnit.MILLISECONDS.convert((end-init), TimeUnit.NANOSECONDS) + " ms");
			System.out.println("Hash: "  + hash);
		}
		
	}
}

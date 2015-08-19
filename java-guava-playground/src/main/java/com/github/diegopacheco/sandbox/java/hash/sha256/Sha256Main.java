package com.github.diegopacheco.sandbox.java.hash.sha256;

import java.security.MessageDigest;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Sha256Main {
	public static void main(String[] args) {
		long init = System.currentTimeMillis();
		String hash = hashSha256(UUID.randomUUID().toString());
		long end = System.currentTimeMillis();
		System.out.println("Hash SHA-256 Speed: " + (end-init) +  " ms"); 
		System.out.println("Hash : " + hash);
		
		for(int i=0;i<9;i++){
			init = System.nanoTime();
			hash = hashSha256(UUID.randomUUID().toString());
			end = System.nanoTime();
			System.out.println("Hash SHA-256 Speed: " + (end-init) +  " nano " + TimeUnit.MILLISECONDS.convert((end-init), TimeUnit.NANOSECONDS) + " ms"); 
			System.out.println("Hash : " + hash);
		}
		
	}
	
	private static String hashSha256(String value){
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-256");
			digest.update(value.getBytes("UTF-8"));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		byte[] hash = digest.digest();
		
		char[] HEX_CHARS = value.toCharArray();
		StringBuilder sb = new StringBuilder(hash.length * 2);
		for (byte b : hash) {
		    sb.append(HEX_CHARS[(b & 0xF0) >> 4]);
		    sb.append(HEX_CHARS[b & 0x0F]);
		}
		return sb.toString();
	}
	
}

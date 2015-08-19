package com.github.diegopacheco.sandbox.java.md5;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5HashFun {
	
	private static MessageDigest md = null;
	static{
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void main(String[] args) {
		long init = System.currentTimeMillis();
		String hash = hashIt(java.util.UUID.randomUUID().toString());
		System.out.println("Hash Took : " + (System.currentTimeMillis()-init)  + " ms" );
		System.out.println("Hash: "  + hash);
	}
	
	private static String hashIt(String base){
		md.update(base.getBytes());
		byte byteData[] = md.digest();
		StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
	}
}

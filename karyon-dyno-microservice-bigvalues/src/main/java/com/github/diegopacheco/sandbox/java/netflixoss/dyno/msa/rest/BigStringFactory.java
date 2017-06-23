package com.github.diegopacheco.sandbox.java.netflixoss.dyno.msa.rest;

import java.text.DecimalFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class creates HUGE strings.
 * 
 * @author diegopacheco
 *
 */
public class BigStringFactory {
	
	private static final Logger logger = LoggerFactory.getLogger(BigStringFactory.class);
	
	public static String create(Integer size){
	
		if (size <= 0) 
			throw new IllegalArgumentException("The String SIZE needs to be bigger than 0. "); 
		
		logger.info("Creating BIG String of size: " + readableFileSize(size));
		
		StringBuilder sb = new StringBuilder(size);
		String paddingStr = "A";
		
		while (sb.length() + paddingStr.length() < size)
			sb.append(paddingStr);

		return sb.toString();
	}
	
	private static String readableFileSize(long size) {
	    if(size <= 0) return "0";
	    final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
	    int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
	    return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}
	
	public static void main(String[] args) {
		
		String str = BigStringFactory.create(2000000);
		System.out.println(str.length());
		
	}
	
}

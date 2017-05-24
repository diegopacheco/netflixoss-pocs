package com.github.diegopacheco.sandbox.java.netflixoss.dyno.msa.dynomite;

import java.util.ArrayList;
import java.util.List;

public class DynomiteSeedsParser {
	
	public static List<DynomiteNodeInfo> parse(String seeds){
		
		List<DynomiteNodeInfo> result = new ArrayList<>();
				
		if (seeds == null || "".equals(seeds)) 
			throw new IllegalArgumentException("Seeds is blank or null. Invalid Seeds! ");
		
		String[] seedsArray =  (seeds.contains("|")) ? seeds.split("\\|") : new String[]{seeds} ;
		if (seedsArray==null || seedsArray.length ==0 )throw new IllegalArgumentException("Invalid Seeds! Seeds: " + seeds);
		
		
		for( String s: seedsArray){
			String[] itens = s.split(":");
			if (itens==null || itens.length ==0 )throw new IllegalArgumentException("Invalid Seeds! Seeds: " + seeds);
			
			DynomiteNodeInfo node = new DynomiteNodeInfo();
			node.setServer(itens[0]);
			node.setPort(itens[1]);
			node.setRack(itens[2]);
			node.setDc(itens[3]);
			node.setTokens(itens[4]);
			result.add(node);
		}
		
		return result;
	}
	
}

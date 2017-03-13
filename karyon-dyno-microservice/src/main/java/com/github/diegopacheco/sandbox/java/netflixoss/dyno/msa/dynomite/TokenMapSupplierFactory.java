package com.github.diegopacheco.sandbox.java.netflixoss.dyno.msa.dynomite;

import java.util.List;
import java.util.Set;

import com.netflix.dyno.connectionpool.Host;
import com.netflix.dyno.connectionpool.TokenMapSupplier;
import com.netflix.dyno.connectionpool.impl.lb.AbstractTokenMapSupplier;

public class TokenMapSupplierFactory {
	
	public static TokenMapSupplier build(List<DynomiteNodeInfo> nodes){
		StringBuilder jsonSB = new StringBuilder("[");
		int count = 0;
		for(DynomiteNodeInfo node: nodes){
			jsonSB.append(" {\"token\":\""+ node.getTokens() 
			                + "\",\"hostname\":\"" + node.getServer() 
							+ "\",\"zone\":\"" +  node.getDc() 
							+ "\"} ");
			count++;
			if (count < nodes.size())
				jsonSB.append(" , ");
		}
		jsonSB.append(" ]\"");
		
	   final String json = jsonSB.toString();
	   TokenMapSupplier testTokenMapSupplier = new AbstractTokenMapSupplier() {
			    @Override
			    public String getTopologyJsonPayload(String hostname) {
			        return json;
			    }
				@Override
				public String getTopologyJsonPayload(Set<Host> activeHosts) {
					return json;
				}
		};
		return testTokenMapSupplier;
	}
	
}
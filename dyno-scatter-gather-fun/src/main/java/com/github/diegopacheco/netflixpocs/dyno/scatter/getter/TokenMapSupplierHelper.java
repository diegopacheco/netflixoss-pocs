package com.github.diegopacheco.netflixpocs.dyno.scatter.getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.netflix.dyno.connectionpool.Host;
import com.netflix.dyno.connectionpool.Host.Status;
import com.netflix.dyno.connectionpool.HostSupplier;
import com.netflix.dyno.connectionpool.TokenMapSupplier;
import com.netflix.dyno.connectionpool.impl.lb.AbstractTokenMapSupplier;

public class TokenMapSupplierHelper {
	
	public static TokenMapSupplier toTokenMapSupplier(List<DynomiteNodeInfo> nodes){
		StringBuilder jsonSB = new StringBuilder("[");
		int count = 0;
		for(DynomiteNodeInfo node: nodes){
			jsonSB.append(" {\"token\":\""+ node.getTokens() 
			                + "\",\"hostname\":\"" + node.getServer()
			                + "\",\"ip\":\"" + node.getServer()			                
							+ "\",\"zone\":\"" +  node.getRack()
							+ "\",\"rack\":\"" +  node.getRack()
							+ "\",\"dc\":\"" +  node.getDc() 
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
				public String getTopologyJsonPayload(java.util.Set<Host> activeHosts) {
					return json;
				}
		};
		return testTokenMapSupplier;
	}
	
	public static HostSupplier toHostSupplier(List<DynomiteNodeInfo> nodes){
		final List<Host> hosts = new ArrayList<Host>();
		
		for(DynomiteNodeInfo node: nodes){
			hosts.add(buildHost(node));
		}
		
		final HostSupplier customHostSupplier = new HostSupplier() {
		   @Override
		   public Collection<Host> getHosts() {
			   return hosts;
		   }
		};
		return customHostSupplier;
	}
	
	public static Host buildHost(DynomiteNodeInfo node){
		Host host =  new Host(node.getServer(), node.getServer(), 8102, node.getRack(), node.getDc(), Status.Up);
		//Host host = new Host(node.getServer(),8102,node.getDc());
		//host.setStatus(Status.Up);
		return host;
	}

	
}

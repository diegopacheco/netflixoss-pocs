package com.github.diegopacheco.sandbox.java.netflixoss.dyno.msa.dynomite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.netflix.dyno.connectionpool.Host;
import com.netflix.dyno.connectionpool.Host.Status;
import com.netflix.dyno.connectionpool.HostSupplier;

public class HostSupplierFactory {
	
	public static HostSupplier build(List<DynomiteNodeInfo> nodes){
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
	
	private static Host buildHost(DynomiteNodeInfo node){
		Host host = new Host(node.getServer(),node.getServer(),8102,node.getRack(),node.getDc(),Status.Up);
		//Host host = new Host(node.getServer(),8102,node.getRack(),Status.Up);
		return host;
	}
	
}
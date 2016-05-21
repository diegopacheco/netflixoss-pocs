package com.github.diegopacheco.sandbox.java.dyno.netflix;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.netflix.dyno.connectionpool.Host;
import com.netflix.dyno.connectionpool.HostSupplier;
import com.netflix.dyno.connectionpool.TokenMapSupplier;
import com.netflix.dyno.connectionpool.Host.Status;
import com.netflix.dyno.connectionpool.impl.lb.AbstractTokenMapSupplier;
import com.netflix.dyno.contrib.ArchaiusConnectionPoolConfiguration;
import com.netflix.dyno.jedis.DynoJedisClient;

public class DynomiteClusterManager {
	
	public static DynoJedisClient connect() {
		final HostSupplier customHostSupplier = new HostSupplier() {
			final List<Host> hosts = new ArrayList<Host>();
			   @Override
			   public Collection<Host> getHosts() {
			    hosts.add(new Host("172.18.0.101", 22222, Status.Up).setRack("default_dc"));
			    hosts.add(new Host("172.18.0.102", 22222, Status.Up).setRack("default_dc"));
			    hosts.add(new Host("172.18.0.103", 22222, Status.Up).setRack("default_dc"));
//				hosts.add(new Host("127.0.0.1", 22222, Status.Up).setRack("localdc"));
			    return hosts;
			   }
		};
		
		final String json = "["
								+ " {\"token\":\"2093391979\",\"hostname\":\"172.18.0.101\",\"zone\":\"default_dc\"}, "
								+ " {\"token\":\"4203913991\",\"hostname\":\"172.18.0.102\",\"zone\":\"default_dc\"}, "
								+ " {\"token\":\"2975898557\",\"hostname\":\"172.18.0.103\",\"zone\":\"default_dc\"} "
							+ " ]\"";
//		final String json = "[{\"token\":\"2865567508\",\"hostname\":\"127.0.0.1\",\"zone\":\"localdc\"}]\"";		
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
		
		DynoJedisClient dynoClient = new DynoJedisClient.Builder()
					.withApplicationName("DYNOMITE_DEMO_APP")
		            .withDynomiteClusterName("MY_CLUSTER")
		            .withCPConfig( new ArchaiusConnectionPoolConfiguration("DYNOMITE_DEMO_APP")
		            					.setPort(8101)
		            					.setLocalDC("localdc")
		            					.withTokenSupplier(testTokenMapSupplier)
		            					.setMaxConnsPerHost(5) )
		            .withHostSupplier(customHostSupplier)
		            .build();
		return dynoClient;
	}
	
}

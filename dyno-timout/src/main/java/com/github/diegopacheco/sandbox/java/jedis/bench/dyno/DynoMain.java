package com.github.diegopacheco.sandbox.java.jedis.bench.dyno;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.netflix.dyno.connectionpool.Host;
import com.netflix.dyno.connectionpool.Host.Status;
import com.netflix.dyno.connectionpool.HostSupplier;
import com.netflix.dyno.connectionpool.TokenMapSupplier;
import com.netflix.dyno.connectionpool.impl.RetryNTimes;
import com.netflix.dyno.connectionpool.impl.lb.AbstractTokenMapSupplier;
import com.netflix.dyno.contrib.ArchaiusConnectionPoolConfiguration;
import com.netflix.dyno.jedis.DynoJedisClient;

public class DynoMain {
	
	public static void main(String[] args) throws Throwable {
		Long init = System.currentTimeMillis();
		try{
			DynoJedisClient cluster = createCluster();
			cluster.set("test", "works");
			System.out.println(cluster.get("test"));
			System.exit(0);
		}catch(Exception e){
			System.out.println(e);
			System.out.println("--");
		}finally {
			Long end = System.currentTimeMillis();
			System.out.println("--");
			System.out.println( "TIME TO RUN: " + TimeUnit.MILLISECONDS.toSeconds((end-init)) + " seconds");
		}
	}
	
	private static DynoJedisClient createCluster() throws Throwable {
		final HostSupplier customHostSupplier = new HostSupplier() {
			final List<Host> hosts = new ArrayList<Host>();
			   @Override
			   public Collection<Host> getHosts() {
			    hosts.add(new Host("200.55.5.1", 22222, Status.Up).setRack("localdc"));
			    return hosts;
			   }
		};
		
		final String json = "["
								+ " {\"token\":\"1383429731\",\"hostname\":\"200.55.5.1\",\"zone\":\"localdc\"}, "
							+ " ]\"";
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
					.withApplicationName("MY_APP")
		            .withDynomiteClusterName("MY_CLUSTER")
		            .withCPConfig( new ArchaiusConnectionPoolConfiguration("MY_APP")
		            					.setPort(8101)
		            					.withTokenSupplier(testTokenMapSupplier)
		            					.setMaxConnsPerHost(100) 
		            					
		            					.setPoolShutdownDelay(0)
		            					.setConnectTimeout(5)
		            					.setFailOnStartupIfNoHosts(true)
		            					.setFailOnStartupIfNoHostsSeconds(5)
		            					.setMaxTimeoutWhenExhausted(5)
		            					.setSocketTimeout(5)
		            					.setRetryPolicyFactory(new RetryNTimes.RetryFactory(2))
		            )
		            .withHostSupplier(customHostSupplier)
		            .build();
		return dynoClient;
	}
	
}

package com.github.diegopacheco.sandbox.java.jedis.bench.dyno;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.netflix.config.ConfigurationManager;
import com.netflix.dyno.connectionpool.Host;
import com.netflix.dyno.connectionpool.Host.Status;
import com.netflix.dyno.connectionpool.HostSupplier;
import com.netflix.dyno.connectionpool.TokenMapSupplier;
import com.netflix.dyno.connectionpool.impl.ConnectionPoolConfigurationImpl;
import com.netflix.dyno.connectionpool.impl.lb.AbstractTokenMapSupplier;
import com.netflix.dyno.jedis.DynoJedisClient;

public class DynoMain {
	
	public static void main(String[] args) throws Throwable {
		Long init = System.currentTimeMillis();
		try{
			DynoJedisClient cluster = createCluster();
			cluster.set("test", "works");
			System.out.println(cluster.get("test"));
		}catch(Exception e){
			System.out.println(e);
			System.out.println("--");
		}finally {
			Long end = System.currentTimeMillis();
			System.out.println("--");
			System.out.println( "TIME TO RUN: " + TimeUnit.MILLISECONDS.toSeconds((end-init)) + " seconds");
			System.exit(0);
		}
	}
	
	private static DynoJedisClient createCluster() throws Throwable {
		final HostSupplier customHostSupplier = new HostSupplier() {
			final List<Host> hosts = new ArrayList<Host>();
			   @Override
			   public Collection<Host> getHosts() {
			    //hosts.add(new Host("200.55.5.1", 22222, Status.Up).setRack("rack1"));
				hosts.add(new Host("240.200.70.7", 22222, "rack1",Status.Up) );
			    return hosts;
			   }
		};
		
		final String json = "["
								+ " {\"token\":\"437425602\",\"hostname\":\"200.55.5.1\",\"zone\":\"rack1\"}, "
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
		
		System.setProperty("EC2_AVAILABILITY_ZONE", "rack1");
		System.setProperty("EC2_REGION", "rack1-local-dc");
		
		ConfigurationManager.getConfigInstance().setProperty("dyno.MY_CLUSTER.connection.connectTimeout", 5000);
		
		
		DynoJedisClient dynoClient = new DynoJedisClient.Builder()
					.withApplicationName("MY_APP")
		            .withDynomiteClusterName("MY_CLUSTER")
		            // ConnectionPoolConfigurationImpl
		            // ArchaiusConnectionPoolConfiguration
		            .withCPConfig( new ConnectionPoolConfigurationImpl("MY_APP")
		            					//.setPort(8101)
		            					.withTokenSupplier(testTokenMapSupplier)
		            					.setMaxConnsPerHost(5) 
		            					
		            					//.setPoolShutdownDelay(0)
		            					.setConnectTimeout(1000)
		            					//.setFailOnStartupIfNoHosts(true)
		            					//.setFailOnStartupIfNoHostsSeconds(12)
	                					//.setMaxTimeoutWhenExhausted(12)
		            					//.setSocketTimeout(12)
		            					//.setRetryPolicyFactory(new RetryNTimes.RetryFactory(2))
		            )
		            .withHostSupplier(customHostSupplier)
		            .build();
		return dynoClient;
		
	}
	
}

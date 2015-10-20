package com.github.diegopacheco.sandbox.java.jedis.bench.dyno.netflix;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.dyno.connectionpool.Host;
import com.netflix.dyno.connectionpool.Host.Status;
import com.netflix.dyno.connectionpool.HostSupplier;
import com.netflix.dyno.connectionpool.TokenMapSupplier;
import com.netflix.dyno.connectionpool.impl.lb.AbstractTokenMapSupplier;
import com.netflix.dyno.contrib.ArchaiusConnectionPoolConfiguration;
import com.netflix.dyno.jedis.DynoJedisClient;

public class DynoJedisMainBench {
	
	private static Logger log = LoggerFactory.getLogger(DynoJedisMainBench.class);
	
	public static void main(String[] args) throws Throwable {
		
//		final HostSupplier customHostSupplier = new HostSupplier() {
//			final List<Host> hosts = new ArrayList<Host>();
//			   @Override
//			   public Collection<Host> getHosts() {
//			    hosts.add(new Host("127.0.0.1", 8102, Status.Up).setRack("us-west-2"));
//			    return hosts;
//			   }
//		};
//		DynoJedisClient dynoClient = new DynoJedisClient.Builder()
//        		.withApplicationName("MY_APP")
//        		.withDynomiteClusterName("us-west-2")
//        		.withHostSupplier(customHostSupplier)
//        		.build();
//		
//		 DynoJedisClient dynoClient = new DynoJedisClient.Builder()
//        .withApplicationName("MY_APP")
//        .withDynomiteClusterName("MY_APP_CLUSTER")
//        .withCPConfig(new ArchaiusConnectionPoolConfiguration("MY_APP")
//                      .setPort(8102)
//                      .setMaxTimeoutWhenExhausted(1000)
//                      .setMaxConnsPerHost(5)
//        ).build();		
		
//		final int port = 8102; 
//		final Host localHost = new Host("127.0.0.1", port, Status.Up);
//		final HostSupplier localHostSupplier = new HostSupplier() {
//			@Override
//			public Collection<Host> getHosts() {
//				return Collections.singletonList(localHost);
//			}
//		};
//		
//		DynoJedisClient dynoClient = new DynoJedisClient.Builder()
//		.withApplicationName("app")
//		.withDynomiteClusterName("app_cluster")
//		.withHostSupplier(localHostSupplier)
//		.withCPConfig(new ConnectionPoolConfigurationImpl("app").setLocalDC("us-west-2"))
//		.withPort(port)
//		.build();

//		final String json = "[{\"token\":\"3303027599\",\"hostname\":\"127.0.0.1\",\"zone\":\"us-west-2\"}]\"";
//		TokenMapSupplier testTokenMapSupplier = new AbstractTokenMapSupplier() {
//		    @Override
//		    public String getTopologyJsonPayload(String hostname) {
//		        return json;
//		    }
//			@Override
//			public String getTopologyJsonPayload(Set<Host> activeHosts) {
//				return json;
//			}
//		};			
		
        System.setProperty("log4j.rootLogger", "ERROR");
        BasicConfigurator.configure();
        log.info("Logger intialized");
		
		final HostSupplier customHostSupplier = new HostSupplier() {
			final List<Host> hosts = new ArrayList<Host>();
			   @Override
			   public Collection<Host> getHosts() {
			    hosts.add(new Host("127.0.0.1", 22222, Status.Up).setRack("localdc"));
			    return hosts;
			   }
		};
		
		final String json = "[{\"token\":\"437425602\",\"hostname\":\"127.0.0.1\",\"zone\":\"localdc\"}]\"";
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
		            					.setLocalDC("localdc")
		            					.withTokenSupplier(testTokenMapSupplier)
		            					.setMaxConnsPerHost(5) )
		            .withHostSupplier(customHostSupplier)
		            .build();
		
		Thread.sleep(5000l);
		
		System.out.println("Setting FOO on Dynomite");
        dynoClient.set("foo", "puneetTest");
        
        System.out.println("Value: " + dynoClient.get("foo"));

        // dynoClient.stopClient();
		System.exit(0);
	}
}

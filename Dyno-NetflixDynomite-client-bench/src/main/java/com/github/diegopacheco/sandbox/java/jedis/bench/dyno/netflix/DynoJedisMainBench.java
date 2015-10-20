package com.github.diegopacheco.sandbox.java.jedis.bench.dyno.netflix;

import java.util.Collection;
import java.util.Collections;

import com.netflix.dyno.connectionpool.Host;
import com.netflix.dyno.connectionpool.Host.Status;
import com.netflix.dyno.connectionpool.HostSupplier;
import com.netflix.dyno.connectionpool.impl.ConnectionPoolConfigurationImpl;
import com.netflix.dyno.jedis.DynoJedisClient;

public class DynoJedisMainBench {
	public static void main(String[] args) {
		
//		final HostSupplier customHostSupplier = new HostSupplier() {
//			final List<Host> hosts = new ArrayList<Host>();
//			   @Override
//			   public Collection<Host> getHosts() {
//			    hosts.add(new Host("127.0.0.1", 8102, Status.Up).setRack("us-west-1a"));
//			    return hosts;
//			   }
//		};
//		DynoJedisClient dynoClient = new DynoJedisClient.Builder()
//        		.withApplicationName("MY_APP")
//        		.withDynomiteClusterName("us-west-1a")
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
		
		final int port = 6379; 
		final Host localHost = new Host("127.0.0.1", port, Status.Up);
		final HostSupplier localHostSupplier = new HostSupplier() {
			@Override
			public Collection<Host> getHosts() {
				return Collections.singletonList(localHost);
			}
		};
		
		DynoJedisClient dynoClient = new DynoJedisClient.Builder()
		.withApplicationName("app")
		.withDynomiteClusterName("app_cluster")
		.withHostSupplier(localHostSupplier)
		.withCPConfig(new ConnectionPoolConfigurationImpl("app").setLocalDC("us-east-1e"))
		.withPort(port)
		.build();

		dynoClient.set("foo", "puneetTest");
		System.out.println("Value: " + dynoClient.get("foo"));

		dynoClient.stopClient();
		System.exit(0);
	}
}

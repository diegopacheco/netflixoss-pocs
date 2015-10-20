package com.github.diegopacheco.sandbox.java.jedis.bench.dyno.netflix;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.netflix.dyno.connectionpool.Host;
import com.netflix.dyno.connectionpool.Host.Status;
import com.netflix.dyno.connectionpool.HostSupplier;
import com.netflix.dyno.jedis.DynoJedisClient;

public class DynoJedisMainBench {
	public static void main(String[] args) {
		
		final HostSupplier customHostSupplier = new HostSupplier() {
			final List<Host> hosts = new ArrayList<Host>();
			   @Override
			   public Collection<Host> getHosts() {
			    hosts.add(new Host("127.0.0.1", 22222, Status.Up).setRack("us-west-1a"));
			    return hosts;
			   }
		};
		
		DynoJedisClient dynoClient = new DynoJedisClient.Builder()
        		.withApplicationName("MY_APP")
        		.withDynomiteClusterName("MY_CLUSTER")
        		.withHostSupplier(customHostSupplier)
        		.build();

		dynoClient.set("foo", "puneetTest");
		System.out.println("Value: " + dynoClient.get("foo"));

		dynoClient.stopClient();
		System.exit(0);
	}
}

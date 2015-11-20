package com.github.diegopacheco.sandbox.java.jedis.bench.dyno.netflix;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.dyno.connectionpool.Host;
import com.netflix.dyno.connectionpool.Host.Status;
import com.netflix.dyno.connectionpool.HostSupplier;
import com.netflix.dyno.connectionpool.TokenMapSupplier;
import com.netflix.dyno.connectionpool.impl.lb.AbstractTokenMapSupplier;
import com.netflix.dyno.contrib.ArchaiusConnectionPoolConfiguration;
import com.netflix.dyno.jedis.DynoJedisClient;

public class DynoClusterJedisAddValueMain {
	
	private static Logger log = LoggerFactory.getLogger(DynoClusterJedisAddValueMain.class);
	
	public static void main(String[] args) throws Throwable {
		System.out.println("Starting... ");
		System.out.println(" Arg 0: " + args[0] + " Arg 1: " + args[1]);
		insert(args[0], args[1]);
		get(args[0]);
		System.exit(0);
	}
	
	private static void printBench(String msg,double init, double end){
		int seconds = (int) ((end - init) / 1000) % 60 ;
		System.out.println("TIME to " + msg + ": " + (end - init) + " ms - " + seconds + " s" );
	}

	private static DynoJedisClient createCluster() throws Throwable {
		final HostSupplier customHostSupplier = new HostSupplier() {
			final List<Host> hosts = new ArrayList<Host>();
			   @Override
			   public Collection<Host> getHosts() {
			    hosts.add(new Host("10.0.1.67", 22222, Status.Up).setRack("localdc"));
			    return hosts;
			   }
		};
		
		final String json = "["
								+ " {\"token\":\"1383429731\",\"hostname\":\"10.0.1.67\",\"zone\":\"localdc\"}, "
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
		            					.setLocalDC("localdc")
		            					.withTokenSupplier(testTokenMapSupplier)
		            					.setMaxConnsPerHost(100) )
		            .withHostSupplier(customHostSupplier)
		            .build();
		return dynoClient;
	}

	private static void insert(String key,String value) throws Throwable {
		DynoJedisClient cluster = createCluster();
		double init = System.currentTimeMillis();
		cluster.set(key, value);
		double end = System.currentTimeMillis();
		printBench("Insert " + key + " - 1 ID with value: " + value,init,end);
	}
	
	private static void get(String key) throws Throwable {
		DynoJedisClient cluster = createCluster();
		double init = System.currentTimeMillis();
		cluster.get(key);
		double end = System.currentTimeMillis();
		printBench("Get 1 key " + key + " ",init,end);
	}
	
}

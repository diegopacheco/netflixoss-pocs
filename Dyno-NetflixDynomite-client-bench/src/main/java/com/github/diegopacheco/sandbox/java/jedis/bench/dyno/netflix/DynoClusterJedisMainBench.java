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

public class DynoClusterJedisMainBench {
	
	private static Logger log = LoggerFactory.getLogger(DynoClusterJedisMainBench.class);
	
	public static void main(String[] args) throws Throwable {
		for(int i=0;i<=2;i++){
			benchInsert(10);
			benchGet(10);
			
			benchInsert(100);
			benchGet(100);
			
			benchInsert(1000);
			benchGet(1000);
			
			benchInsert(10000);
			benchGet(10000);
			
			benchInsert(100000);
			benchGet(100000);
		}
		
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
			    hosts.add(new Host("127.0.0.1", 22222, Status.Up).setRack("localdc"));
			    return hosts;
			   }
		};
		
		final String json = "[{\"token\":\"1383429731\",\"hostname\":\"127.0.0.1\",\"zone\":\"localdc\"}]\"";
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

	private static void benchInsert(int n) throws Throwable {
		DynoJedisClient cluster = createCluster();
		
		double init = System.currentTimeMillis();
		for(int i=0;i<=(n-1);i++){
			cluster.set("key"+i, "value"+i);
		}
		double end = System.currentTimeMillis();
		printBench("Insert " + n + " IDS ",init,end);
	}
	
	private static void benchGet(int n) throws Throwable {
		DynoJedisClient cluster = createCluster();
		
		double init = System.currentTimeMillis();
		for(int i=0;i<=(n-1);i++){
			cluster.get("key"+i);
		}
		double end = System.currentTimeMillis();
		printBench("Get " + n + " IDS ",init,end);
	}
	
}

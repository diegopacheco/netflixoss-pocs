package com.github.diegopacheco.netflixpocs.dyno.scatter.getter;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.netflix.config.ConfigurationManager;
import com.netflix.dyno.connectionpool.Operation;
import com.netflix.dyno.connectionpool.OperationResult;
import com.netflix.dyno.connectionpool.impl.RetryNTimes;
import com.netflix.dyno.contrib.ArchaiusConnectionPoolConfiguration;
import com.netflix.dyno.jedis.DynoJedisClient;

import redis.clients.jedis.Jedis;

public class Main {
	public static void main(String[] args) {
		
		ConfigurationManager.getConfigInstance().setProperty("dynomite.driver.seeds", "179.18.0.101:8102:rack1:dc:100|179.18.0.102:8102:rack2:dc:100|179.18.0.103:8102:rack3:dc:100|179.18.0.201:8102:rack1:dc:200|179.18.0.202:8102:rack2:dc:200|179.18.0.203:8102:rack3:dc:200");
		ConfigurationManager.getConfigInstance().setProperty("dynomite.driver.cluster.name","dynomiteCluster");
		ConfigurationManager.getConfigInstance().setProperty("dynomite.driver.client.name","dynomiteCluster"); 
		ConfigurationManager.getConfigInstance().setProperty("dyno.dyn_o_mite.retryPolicy","RetryNTimes:3:true");
		
		String client = ConfigurationManager.getConfigInstance().getString("dynomite.driver.client.name","");
		String cluster = ConfigurationManager.getConfigInstance().getString("dynomite.driver.cluster.name","");
		String seeds = ConfigurationManager.getConfigInstance().getString("dynomite.driver.seeds","");
		List<DynomiteNodeInfo> nodes = DynomiteSeedsParser.parse(seeds);
		
		DynoJedisClient dynoClient = new DynoJedisClient.Builder()
				.withApplicationName(client)
	            .withDynomiteClusterName(cluster)
	            .withCPConfig( new ArchaiusConnectionPoolConfiguration(client)
	            					.withTokenSupplier(TokenMapSupplierHelper.toTokenMapSupplier(nodes))
	            					.setMaxConnsPerHost(1)
                                    .setConnectTimeout(2000)
                                    .setRetryPolicyFactory(new RetryNTimes.RetryFactory(3,true))
	            )
	            .withHostSupplier(TokenMapSupplierHelper.toHostSupplier(nodes))
	            .build();
		
		dynoClient.set("x", "100");
		System.out.println("keys x*: " + dynoClient.keys("x*"));
		
		Collection<OperationResult<Set<String>>>  scatterGetter =  dynoClient.getConnPool().executeWithRing(new Operation<Jedis, Set<String>>() {
			public Set<String> execute(Jedis client, com.netflix.dyno.connectionpool.ConnectionContext state) throws com.netflix.dyno.connectionpool.exception.DynoException {
				client.set("x" + (Math.random() * 10000), "value"+(Math.random() * 10000));
				return client.keys("x*");
			}
			@Override
			public String getName() {
				return "keys";
			}
			@Override
			public String getKey() {
				return "1";
			};
		});
		System.out.println("Scatter Getter Results");
		Iterator<OperationResult<Set<String>>> it = scatterGetter.iterator();
		while(it.hasNext()){
			System.out.println(it.next().getResult());
		}
		
	}
}

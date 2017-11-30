package com.github.diegopacheco.netflixoss.pocs.dyno;

import java.util.List;

import com.netflix.config.ConfigurationManager;
import com.netflix.dyno.connectionpool.impl.RetryNTimes;
import com.netflix.dyno.contrib.ArchaiusConnectionPoolConfiguration;
import com.netflix.dyno.jedis.DynoJedisClient;

/**
 * Builds Dyno Connection
 * 
 * @author diegopacheco
 * @since 01/07/2016
 * @version 1.0
 */
public class DynoConnectionManager {
	
	public static DynoJedisClient build() {
		ConfigurationManager.getConfigInstance().setProperty("dynomite.driver.seeds", "179.18.0.101:8102:rack1:dc:100|179.18.0.102:8102:rack2:dc:100|179.18.0.103:8102:rack3:dc:100");
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
		return dynoClient;
	}
	
}

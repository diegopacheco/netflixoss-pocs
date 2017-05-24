package com.github.diegopacheco.sandbox.java.netflixoss.dyno.msa.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.diegopacheco.sandbox.java.netflixoss.dyno.msa.dynomite.DynomiteNodeInfo;
import com.github.diegopacheco.sandbox.java.netflixoss.dyno.msa.dynomite.DynomiteSeedsParser;
import com.github.diegopacheco.sandbox.java.netflixoss.dyno.msa.dynomite.HostSupplierFactory;
import com.github.diegopacheco.sandbox.java.netflixoss.dyno.msa.dynomite.TokenMapSupplierFactory;
import com.google.inject.Singleton;
import com.netflix.config.ConfigurationManager;
import com.netflix.dyno.connectionpool.HostSupplier;
import com.netflix.dyno.connectionpool.TokenMapSupplier;
import com.netflix.dyno.connectionpool.impl.RetryNTimes;
import com.netflix.dyno.contrib.ArchaiusConnectionPoolConfiguration;
import com.netflix.dyno.jedis.DynoJedisClient;

/**
 * 
 * @author diegopacheco
 *
 */
@Singleton
public class DynoManager {
	
	private static final Logger logger = LoggerFactory.getLogger(DynoManager.class);
	
	private DynoJedisClient dyno;
	
	public DynoManager() {}
	
	public DynoJedisClient getClient(){
		if (dyno==null){
			// With or Without this line does not make any difference. It works(SLOW) but it works.
			ConfigurationManager.getConfigInstance().setProperty("dyno.dynomiteCluster.retryPolicy","RetryNTimes:3:true");
			
			String seeds = System.getenv("DYNOMITE_SEEDS");
			logger.info("Using Seeds: " + seeds );

			List<DynomiteNodeInfo> nodes = DynomiteSeedsParser.parse(seeds);
			TokenMapSupplier tms = TokenMapSupplierFactory.build(nodes);
			HostSupplier hs = HostSupplierFactory.build(nodes);
			
			DynoJedisClient dynoClient = new DynoJedisClient.Builder().withApplicationName("dynomiteCluster")
					.withDynomiteClusterName("dynomiteCluster")
					.withCPConfig(new ArchaiusConnectionPoolConfiguration("dynomiteCluster")
							 	.withTokenSupplier(tms)
							    .setMaxConnsPerHost(1)
							    .setConnectTimeout(2000)
							    .setRetryPolicyFactory(
									new RetryNTimes.RetryFactory(3,true)
					))
					.withHostSupplier(hs).build();
			this.dyno = dynoClient;
		}
		return dyno;
		
	}
	
}

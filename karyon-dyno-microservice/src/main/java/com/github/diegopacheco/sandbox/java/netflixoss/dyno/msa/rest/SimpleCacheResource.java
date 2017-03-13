package com.github.diegopacheco.sandbox.java.netflixoss.dyno.msa.rest;

import java.util.List;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.diegopacheco.sandbox.java.netflixoss.dyno.msa.dynomite.DynomiteNodeInfo;
import com.github.diegopacheco.sandbox.java.netflixoss.dyno.msa.dynomite.DynomiteSeedsParser;
import com.github.diegopacheco.sandbox.java.netflixoss.dyno.msa.dynomite.HostSupplierFactory;
import com.github.diegopacheco.sandbox.java.netflixoss.dyno.msa.dynomite.TokenMapSupplierFactory;
import com.netflix.dyno.connectionpool.HostSupplier;
import com.netflix.dyno.connectionpool.TokenMapSupplier;
import com.netflix.dyno.connectionpool.impl.RetryNTimes;
import com.netflix.dyno.contrib.ArchaiusConnectionPoolConfiguration;
import com.netflix.dyno.jedis.DynoJedisClient;

@Singleton
@Path("/cache")
public class SimpleCacheResource {

	private static final Logger logger = LoggerFactory.getLogger(SimpleCacheResource.class);
	
	
	private DynoJedisClient dyno;
	
	public SimpleCacheResource() {
		
		String seeds = System.getenv("DYNOMITE_SEEDS");
		logger.info("Using Seeds: " + seeds );

		List<DynomiteNodeInfo> nodes = DynomiteSeedsParser.parse(seeds);
		TokenMapSupplier tms = TokenMapSupplierFactory.build(nodes);
		HostSupplier hs = HostSupplierFactory.build(nodes);
		
		DynoJedisClient dynoClient = new DynoJedisClient.Builder().withApplicationName("dynomiteCluster")
				.withDynomiteClusterName("dynomiteCluster")
				.withCPConfig(new ArchaiusConnectionPoolConfiguration("dynomiteCluster")
						.withTokenSupplier(tms).setMaxConnsPerHost(1).setConnectTimeout(2000)
						.setRetryPolicyFactory(new RetryNTimes.RetryFactory(1)))
				.withHostSupplier(hs).build();
		
		this.dyno = dynoClient;
		
	}
	
	@GET
	@Path("set/{k}/{v}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response set(@PathParam("k") String k,@PathParam("v") String v) {
		try {
			dyno.set(k, v);
			return Response.ok("200").build();
		} catch (Exception e) {
			logger.error("Error creating json response.", e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GET
	@Path("get/{k}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("k") String k) {
		try {
			return Response.ok( dyno.get(k) ).build();
		} catch (Exception e) {
			logger.error("Error creating json response.", e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

}

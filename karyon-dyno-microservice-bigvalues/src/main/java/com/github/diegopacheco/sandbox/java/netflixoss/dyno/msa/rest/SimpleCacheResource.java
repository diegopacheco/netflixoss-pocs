package com.github.diegopacheco.sandbox.java.netflixoss.dyno.msa.rest;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@Path("/cache")
public class SimpleCacheResource {

	private static final Logger logger = LoggerFactory.getLogger(SimpleCacheResource.class);
	
	private DynoManager dynoManager;
	
	@Inject
	public SimpleCacheResource(DynoManager dynoManager) {
		this.dynoManager = dynoManager;
	}
	
	@GET
	@Path("set/{k}/{s}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response set(@PathParam("k") String k,@PathParam("s") String s) {
		try {

			String bigString = BigStringFactory.create(new Integer(s));
			dynoManager.getClient().set(k, bigString);
			bigString = null;
			
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
			String result = dynoManager.getClient().get(k);
			if (result != null) result = BigStringFactory.readableFileSize(result.length());
			
			return Response.ok( result ).build();
		} catch (Exception e) {
			logger.error("Error creating json response.", e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

}

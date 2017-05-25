package com.github.diegopacheco.sandbox.java.netflixoss.msa.rest;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.entities.Subsegment;
import com.google.inject.Singleton;

@Singleton
@Path("/cache")
public class SimpleCacheResource {

	private static final Logger logger = LoggerFactory.getLogger(SimpleCacheResource.class);
	private static Map<String,String> cache = new HashMap<>();
	
	@GET
	@Path("set/{k}/{v}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response set(@PathParam("k") String k,@PathParam("v") String v) {
		Subsegment subsegment = AWSXRay.beginSubsegment("## Cache.set");
		try {
			cache.put(k, v);
			return Response.ok("200").build();
		} catch (Exception e) {
			subsegment.addException(e);
			logger.error("Error creating json response.", e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}finally{
			AWSXRay.endSubsegment();
		}
	}
	
	@GET
	@Path("get/{k}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("k") String k) {
		Subsegment subsegment = AWSXRay.beginSubsegment("## Cache.get");
		try {
			return Response.ok( cache.get(k) ).build();
		} catch (Exception e) {
			subsegment.addException(e);
			logger.error("Error creating json response.", e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}finally{
			AWSXRay.endSubsegment();
		}
	}

}
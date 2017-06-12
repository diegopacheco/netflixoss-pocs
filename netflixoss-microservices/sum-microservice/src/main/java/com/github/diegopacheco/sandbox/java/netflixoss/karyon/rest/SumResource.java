package com.github.diegopacheco.sandbox.java.netflixoss.karyon.rest;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

@Singleton
@Path("/math")
public class SumResource {

	private static final Logger logger = LoggerFactory.getLogger(SumResource.class);
	
	private SumService service;

	@Inject
	public SumResource(SumService service) {
		this.service = service;
	}
	
	@GET
	@Path("sum/{a}/{b}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response set(@PathParam("a") Double a,@PathParam("b") Double b) {
		try {
			return Response.ok( service.sum(a, b) + "" ).build();
		} catch (Exception e) {
			logger.error("Error creating json response.", e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
}

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
public class DivResource {

	private static final Logger logger = LoggerFactory.getLogger(DivResource.class);
	
	private DivService service;
	
	@Inject
	public DivResource(DivService service) {
		this.service = service;
	}
	
	@GET
	@Path("div/{a}/{b}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response set(@PathParam("a") Double a,@PathParam("b") Double b) {
		try {
			return Response.ok( service.div(a, b) + "" ).build();
		} catch (Exception e) {
			logger.error("Error creating json response.", e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

}

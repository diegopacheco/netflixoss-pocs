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

@Singleton
@Path("/math")
public class CalcResource {

	private static final Logger logger = LoggerFactory.getLogger(CalcResource.class);
	
	private CalcService service;
	
	public CalcResource(CalcService service) {
		this.service = service;
	}
	
	@GET
	@Path("calc/{expr}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response set(@PathParam("expr") String expr) {
		try {
			return Response.ok( service.calc(expr)  ).build();
		} catch (Exception e) {
			logger.error("Error creating json response.", e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	

}

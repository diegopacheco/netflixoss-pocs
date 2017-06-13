package com.github.diegopacheco.sandbox.java.netflixoss.karyon.rest;

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

import com.github.diegopacheco.sandbox.java.netflixoss.karyon.hystrix.SimpleCommand;

@Singleton
@Path("/math")
public class CalcResource {

	private static final Logger logger = LoggerFactory.getLogger(CalcResource.class);
	
	private CalcService service;
	
	@Inject
	public CalcResource(CalcService service) {
		this.service = service;
	}
	
	@GET
	@Path("calc/{expr}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response set(@PathParam("expr") String expr) {
		try {
			return Response.ok( service.calc(expr) + ""  ).
							header("CREATED_BY", ArchaiusPropsManager.getInstance().getCreator()).
							build();
		} catch (Exception e) {
			logger.error("Error creating json response.", e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GET
	@Path("ops")
	@Produces(MediaType.TEXT_PLAIN)
	public Response ops() {
		try {
			
			String result = new SimpleCommand("+-/*").observe().toBlocking().first();
			
			return Response.ok( result ).
							header("CREATED_BY", ArchaiusPropsManager.getInstance().getCreator()).
							build();
		} catch (Exception e) {
			logger.error("Error creating json response.", e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	

}

package com.github.diegopacheco.sandbox.java.netflixoss.karyon.poc.jersey;

import java.util.Date;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.diegopacheco.sandbox.java.netflixoss.karyon.poc.hystrix.DateHystrixCommand;
import com.github.diegopacheco.sandbox.java.netflixoss.karyon.poc.jersey.ex.DivisionByZeroException;
import com.netflix.config.ConfigurationManager;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandMetrics;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;

@Singleton
@Path("/math")
public class SimpleMathResource {

	private static final Logger logger = LoggerFactory.getLogger(SimpleMathResource.class);

	public SimpleMathResource() {
		ConfigurationManager.getConfigInstance().setProperty("hystrix.threadpool.default.coreSize", 8);
		ConfigurationManager.getConfigInstance()
		    .setProperty("hystrix.command.DateHystrixCommand.execution.isolation.thread.timeoutInMilliseconds", 3000);
		ConfigurationManager.getConfigInstance().setProperty("hystrix.command.default.metrics.rollingPercentile.numBuckets",
		    10);
	}

	@GET
	@Path("sum/{v1}/{v2}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response sum(@PathParam("v1") Double v1, @PathParam("v2") Double v2) {

		HystrixRequestContext context = null;
		try {

			context = HystrixRequestContext.initializeContext();

			Date d = new DateHystrixCommand("DateHystrixCommand").execute();
			HystrixCommandMetrics dateMetrics = HystrixCommandMetrics
			    .getInstance(HystrixCommandKey.Factory.asKey("DateHystrixCommand"));

			JSONObject response = new JSONObject();
			response.put("Message", "Sum [" + v1 + "] + [" + v2 + "] + = " + (v1 + v2) + " | from Netflix OSS - Date: "
			    + d.toString() + " - " + dateMetrics);
			return Response.ok(response.toString()).build();

		} catch (JSONException e) {
			logger.error("Error creating json response.", e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		} finally {
			context.shutdown();
		}
	}
	
	@GET
	@Path("div/{v1}/{v2}")
	@Produces(MediaType.APPLICATION_JSON)
	public String div(@PathParam("v1") Double v1, @PathParam("v2") Double v2) {
			
			if(v1==0 || v2==0)
				throw new DivisionByZeroException("V1 or V2 cannot be ZERO");
		
			JSONObject response = new JSONObject();
			try {
				 response.put("Message", "Div [" + v1 + "] / [" + v2 + "] = " + (v1 / v2));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return response.toString();

	}

	public String sumRx(Double v1, Double v2) {

		HystrixRequestContext context = null;
		try {

			context = HystrixRequestContext.initializeContext();

			JSONObject response = new JSONObject();
			response.put("Message", "Div [" + v1 + "] / [" + v2 + "] = " + (v1 / v2) + " | from Netflix OSS");
			return response.toString();

		} catch (JSONException e) {
			logger.error("Error creating json response.", e);
			return "Error creating json response. " + e;
		} finally {
			context.shutdown();
		}
	}

}

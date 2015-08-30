package com.github.diegopacheco.sandbox.java.netflix.pocs.karyon.eureka.client.rest;

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

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

@Singleton
@Path("/weather")
public class WeatherResource {
	
	private static final Logger logger = LoggerFactory.getLogger(WeatherResource.class);
	
	@GET
    @Path("now/{location}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response sum(@PathParam("location") String location) {
        try {
        	
        	  ClientConfig cc = new DefaultClientConfig();
        	  cc.getProperties().put(ClientConfig.PROPERTY_FOLLOW_REDIRECTS, true);
        	  Client c = Client.create(cc);
        	  WebResource r = c.resource("http://api.openweathermap.org/data/2.5/weather?q=" + location);
    	      
        	  r.accept(MediaType.APPLICATION_JSON_TYPE).get(String.class);
    	      ClientResponse cr = r.get(ClientResponse.class);
    	      String entity = cr.getEntity(String.class);
    	      
         	  JSONObject response = new JSONObject();
              response.put("weather",  entity);
          
            return Response.ok(response.toString()).build();
        } catch (JSONException e) {
            logger.error("Error creating json response.", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
	
}


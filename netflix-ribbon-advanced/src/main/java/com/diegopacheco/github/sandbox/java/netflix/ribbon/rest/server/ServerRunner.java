package com.diegopacheco.github.sandbox.java.netflix.ribbon.rest.server;

import javax.ws.rs.core.UriBuilder;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

public class ServerRunner {
  public static void main(String[] args) throws Exception {
	  
	  ResourceConfig rc = new ResourceConfig();
      rc.registerClasses(SimpleResource.class);
      
      HttpServer server = GrizzlyHttpServerFactory.createHttpServer(
    		  UriBuilder.fromUri("https://localhost/").port(7001).build(),
              rc,
              true);
      server.start();
      
  }
}

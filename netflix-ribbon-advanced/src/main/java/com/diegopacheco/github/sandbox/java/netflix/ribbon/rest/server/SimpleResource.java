package com.diegopacheco.github.sandbox.java.netflix.ribbon.rest.server;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("api")
public class SimpleResource {
	
	@GET
    @Produces(MediaType.APPLICATION_JSON)
    public String get(@HeaderParam("MY_HEADER") String myHeader) {
        return "Api REST says hi! Header: " + myHeader;
    }
	
}

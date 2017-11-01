package com.github.diegopacheco.netflix.pocs.runtime.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/works")
public class SimpleResource {
	
	@GET
	public String doWork(){
		return "Works!";
	}
	
}

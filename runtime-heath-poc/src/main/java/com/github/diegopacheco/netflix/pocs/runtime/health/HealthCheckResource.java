
package com.github.diegopacheco.netflix.pocs.runtime.health;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.netflix.runtime.health.api.HealthCheckAggregator;

@Path("/health")
public class HealthCheckResource {
	
	private HealthCheckAggregator healthCheck;
	
	@Inject
	public HealthCheckResource(HealthCheckAggregator healthCheck) {
		this.healthCheck = healthCheck;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response doCheck() {
		try {
			return Response.status(201).entity(healthCheck.check().get()).build();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
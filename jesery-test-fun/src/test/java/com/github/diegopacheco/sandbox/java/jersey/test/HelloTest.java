package com.github.diegopacheco.sandbox.java.jersey.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

public class HelloTest extends JerseyTest {

	@Path("hello")
	@Singleton
	public static class HelloResource {
		@GET
		public String getHello() {
			return "Hello World!";
		}
	}

	@Override
	protected Application configure() {
		return new ResourceConfig(HelloResource.class);
	}

	@Test
	public void test() {
		final String hello = target("hello").request().get(String.class);
		assertEquals("Hello World!", hello);
	}

	@Test
	public void testResponse(){
		final Response response = target("hello").request().get();

		assertEquals(response.getStatus(), 200);
		assertTrue("Hello World!".equals(response.readEntity(String.class)));
	}

}	

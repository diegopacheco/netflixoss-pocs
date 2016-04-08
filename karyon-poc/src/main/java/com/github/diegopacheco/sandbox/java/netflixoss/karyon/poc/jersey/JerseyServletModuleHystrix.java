package com.github.diegopacheco.sandbox.java.netflixoss.karyon.poc.jersey;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import com.sun.jersey.guice.JerseyServletModule;

public class JerseyServletModuleHystrix extends JerseyServletModule {
	
	@Override
	protected void configureServlets() {
		serve("/hystrix.stream").with(HystrixMetricsStreamServlet.class);
	}
	
}

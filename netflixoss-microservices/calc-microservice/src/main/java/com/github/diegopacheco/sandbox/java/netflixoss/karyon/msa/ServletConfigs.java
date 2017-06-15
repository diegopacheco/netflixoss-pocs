package com.github.diegopacheco.sandbox.java.netflixoss.karyon.msa;

import com.google.inject.servlet.ServletModule;
import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;

public class ServletConfigs extends ServletModule {

	@Override
	protected void configureServlets() {
		super.configureServlets();
		serve("/hystrix.stream").with(HystrixMetricsStreamServlet.class);
	}

}

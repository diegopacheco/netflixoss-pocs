package com.github.diegopacheco.sandbox.java.netflixoss.karyon.rest;

import com.github.diegopacheco.sandbox.java.netflixoss.karyon.ribbon.RibbonClient;
import com.google.inject.Inject;

public class CalcService {
	
	private RibbonClient client;
	
	@Inject
	public CalcService(RibbonClient client) {
		this.client = client;
	}
	
	public Double calc(String expr){
		return client.sum(20.0, 30.0).toBlocking().first();
	}
	
}

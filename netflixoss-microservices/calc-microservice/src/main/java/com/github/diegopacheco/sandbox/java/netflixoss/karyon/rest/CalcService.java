package com.github.diegopacheco.sandbox.java.netflixoss.karyon.rest;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.github.diegopacheco.sandbox.java.netflixoss.karyon.ribbon.RibbonMathClient;

@Singleton
public class CalcService {
	
	private RibbonMathClient client;
	
	@Inject
	public CalcService(RibbonMathClient client) {
		this.client = client;
	}
	
	public Double calc(String expr){
		return client.sum(20.0, 30.0).toBlocking().first();
	}
	
}

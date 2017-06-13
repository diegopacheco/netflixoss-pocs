package com.github.diegopacheco.sandbox.java.netflixoss.karyon.msa;

import com.github.diegopacheco.sandbox.java.netflixoss.karyon.rest.CalcService;
import com.github.diegopacheco.sandbox.java.netflixoss.karyon.ribbon.RibbonMathClient;
import com.google.inject.AbstractModule;

public class GuiceBindingsModule extends AbstractModule{

	@Override
	protected void configure() {
		bind(RibbonMathClient.class);
    	bind(CalcService.class);
	}
	
}

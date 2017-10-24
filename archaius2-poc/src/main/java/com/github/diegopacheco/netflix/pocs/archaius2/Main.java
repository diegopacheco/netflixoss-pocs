package com.github.diegopacheco.netflix.pocs.archaius2;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main {
	
	//
	// -Dapp.name.simple=hijacked on VM arguments also works :-)
	//
	public static void main(String[] args) {
		
		 Injector injector = Guice.createInjector(new GuiceArchaiusModule());
		 SimpleService service = injector.getInstance(SimpleService.class);
		 service.doWork();
		 
	}
}

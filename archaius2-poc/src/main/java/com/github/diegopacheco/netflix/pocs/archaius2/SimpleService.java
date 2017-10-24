package com.github.diegopacheco.netflix.pocs.archaius2;

import javax.inject.Inject;

public class SimpleService {

	private AppConfig appConfig;
	
	@Inject
	public SimpleService(AppConfig appConfig) {
		this.appConfig= appConfig;
	}
	
	public void doWork(){
		System.out.println("Working... Name: " + appConfig.getNameSimple() + 
							" - Persistence: " + appConfig.isPersistenceEnable() +
							" - JavaHome   : " + appConfig.getJavaHome()			
				);
	}
	
}	

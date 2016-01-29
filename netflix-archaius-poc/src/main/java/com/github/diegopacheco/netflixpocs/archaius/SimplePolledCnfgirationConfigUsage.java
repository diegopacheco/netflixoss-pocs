package com.github.diegopacheco.netflixpocs.archaius;

import java.io.File;

import com.netflix.config.AbstractPollingScheduler;
import com.netflix.config.ConfigurationManager;
import com.netflix.config.DynamicConfiguration;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;
import com.netflix.config.FixedDelayPollingScheduler;
import com.netflix.config.PolledConfigurationSource;
import com.netflix.config.sources.URLConfigurationSource;

public class SimplePolledCnfgirationConfigUsage {
	
	public static void main(String[] args) throws Throwable{
		polledCnfgiration();
	}

	private static void polledCnfgiration() throws Throwable {
		
		System.setProperty("archaius.configurationSource.additionalUrls", 
				"file:///" + new File(".").getCanonicalPath() + "/aditional-resources/aditional-configs.properties");
		
		PolledConfigurationSource source = new URLConfigurationSource();
		AbstractPollingScheduler scheduler = new FixedDelayPollingScheduler(1000,1000,true);
		ConfigurationManager.install(new DynamicConfiguration(source, scheduler));
		
		DynamicStringProperty prop = DynamicPropertyFactory.getInstance().getStringProperty("netflix.poc.a", "");
		prop.addCallback(new Runnable() {
			@Override
			public void run() {
				System.out.println("polledCnfgiration: OMG my property just changed: " + 
						DynamicPropertyFactory.getInstance().getStringProperty("netflix.poc.a", ""));
			}
		});
		System.out.println("polledCnfgiration: My Prop is:" + prop.get());
		ConfigurationManager.getConfigInstance().setProperty("netflix.poc.a", "10");
		
		while(true) Thread.sleep(3000);
		// Go change the file meanwhile and see :-)
	}
	
}

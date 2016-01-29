package com.github.diegopacheco.netflixpocs.archaius;

import com.netflix.config.ConcurrentMapConfiguration;
import com.netflix.config.ConfigurationManager;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;

public class SimpleConcurrentMapConfigArchaiusConfigUsage {
	
	public static void main(String[] args) {
		concurrentMapConfig();
	}

	private static void concurrentMapConfig() {
		ConcurrentMapConfiguration configuration = new ConcurrentMapConfiguration();
		ConfigurationManager.install(configuration);
		
		DynamicStringProperty prop = DynamicPropertyFactory.getInstance().getStringProperty("netflix.poc.a", "");
		prop.addCallback(new Runnable() {
			@Override
			public void run() {
				System.out.println("concurrentMapConfig: OMG my property just changed");
			}
		});
		System.out.println("concurrentMapConfig: My Prop is:" + prop.get());
		
		ConfigurationManager.getConfigInstance().setProperty("netflix.poc.a", "10");
	}
	
}

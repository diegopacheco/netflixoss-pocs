package com.github.diegopacheco.netflixpocs.archaius;

import java.io.File;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;

import com.netflix.config.AbstractPollingScheduler;
import com.netflix.config.ConcurrentCompositeConfiguration;
import com.netflix.config.ConcurrentMapConfiguration;
import com.netflix.config.ConfigurationManager;
import com.netflix.config.DynamicConfiguration;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;
import com.netflix.config.FixedDelayPollingScheduler;
import com.netflix.config.PolledConfigurationSource;
import com.netflix.config.sources.URLConfigurationSource;

public class ComplexConfigurationUsage {
	
	public static void main(String[] args) throws Throwable{
		complexConfgiration();
	}

	private static void complexConfgiration() throws Throwable {
		
	 	ConcurrentMapConfiguration configFromPropertiesFile =
		      new ConcurrentMapConfiguration(new PropertiesConfiguration("my-configs.properties"));
		 
		// configuration from system properties
		ConcurrentMapConfiguration configFromSystemProperties = 
		      new ConcurrentMapConfiguration(new SystemConfiguration());
		  
		// configuration from a dynamic source
		System.setProperty("archaius.configurationSource.additionalUrls", 
					"file:///" + new File(".").getCanonicalPath() + "/aditional-resources/aditional-configs.properties");
		 
		// configuration from a dynamic source 
		PolledConfigurationSource source = new URLConfigurationSource();
		AbstractPollingScheduler scheduler = new FixedDelayPollingScheduler(1000,1000,true);
		DynamicConfiguration dynamicConfiguration = new DynamicConfiguration(source, scheduler);  
		
		// create a hierarchy of configuration that makes
		// 1) dynamic configuration source override system properties and,
		// 2) system properties override properties file
		ConcurrentCompositeConfiguration finalConfig = new ConcurrentCompositeConfiguration();
		finalConfig.addConfiguration(dynamicConfiguration, "dynamicConfig");
		finalConfig.addConfiguration(configFromSystemProperties, "systemConfig");
		finalConfig.addConfiguration(configFromPropertiesFile, "fileConfig");
	
		// install with ConfigurationManager so that finalConfig
		// becomes the source of dynamic properties
		ConfigurationManager.install(finalConfig); 
		
		DynamicStringProperty prop = DynamicPropertyFactory.getInstance().getStringProperty("netflix.poc.a", "");
		prop.addCallback(new Runnable() {
			@Override
			public void run() {
				System.out.println("complexConfiguration: OMG my property just changed: " + 
						DynamicPropertyFactory.getInstance().getStringProperty("netflix.poc.a", ""));
			}
		});
		System.out.println("complexConfiguration: My Prop is:" + prop.get());
		
		// Nothing happens
		ConfigurationManager.getConfigInstance().setProperty("netflix.poc.a", "10");
		System.out.println("1: " + ConfigurationManager.getConfigInstance().getProperty("netflix.poc.a"));
		
		// Nothing happens
		System.setProperty("netflix.poc.a", "system");
		System.out.println("2: " + ConfigurationManager.getConfigInstance().getProperty("netflix.poc.a"));
		
		while(true) Thread.sleep(3000);
		// Go change the file meanwhile and see :-) = Stuff Happens
	}
}

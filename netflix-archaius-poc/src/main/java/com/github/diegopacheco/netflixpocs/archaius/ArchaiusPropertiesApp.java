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

public class ArchaiusPropertiesApp {
	public static void main(String[] args) throws Throwable{
		
		// You can pass all this to the JVM with -D
		System.setProperty("archaius.deployment.environment","dev");
		System.setProperty("archaius.deployment.region","local");
		System.setProperty("archaius.deployment.datacenter","local");
		System.setProperty("archaius.deployment.applicationId","my-console-app1");
		System.setProperty("archaius.deployment.serverId","server1");
		System.setProperty("archaius.deployment.stack","diegoMSAArchStack");
		
		String env = ConfigurationManager.getConfigInstance().getString("@environment");
		String dc = ConfigurationManager.getConfigInstance().getString("@datacenter");
		
		System.setProperty("archaius.configurationSource.additionalUrls", 
				"file:///" + new File(".").getCanonicalPath() + 
				"/aditional-resources/conf-" + env + ".properties");
		
		PolledConfigurationSource source = new URLConfigurationSource();
		AbstractPollingScheduler scheduler = new FixedDelayPollingScheduler(1000,1000,true);
		ConfigurationManager.install(new DynamicConfiguration(source, scheduler));
		
		DynamicStringProperty prop = DynamicPropertyFactory.getInstance().
				getStringProperty("netflix.poc." + dc + ".data", "");
		prop.addCallback(new Runnable() {
			@Override
			public void run() {
				System.out.println("polledCnfgiration: OMG my property just changed: " + 
						DynamicPropertyFactory.getInstance().
						getStringProperty("netflix.poc." + dc + ".data", ""));
			}
		});
		System.out.println("polledCnfgiration: My Prop is:" + prop.get());
		ConfigurationManager.getConfigInstance().setProperty("netflix.poc." + dc + ".data", "10");
		
		while(true) Thread.sleep(3000);
		// Go change the file meanwhile and see :-)
		
	}
}

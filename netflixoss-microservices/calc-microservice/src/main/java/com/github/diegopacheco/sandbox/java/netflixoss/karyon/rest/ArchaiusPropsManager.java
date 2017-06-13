package com.github.diegopacheco.sandbox.java.netflixoss.karyon.rest;

import java.io.File;

import com.netflix.config.AbstractPollingScheduler;
import com.netflix.config.ConfigurationManager;
import com.netflix.config.DynamicConfiguration;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;
import com.netflix.config.FixedDelayPollingScheduler;
import com.netflix.config.PolledConfigurationSource;
import com.netflix.config.sources.URLConfigurationSource;

public class ArchaiusPropsManager {
	
	private static ArchaiusPropsManager instance;
	
	public static ArchaiusPropsManager getInstance(){
		if (instance==null){
			instance = new ArchaiusPropsManager();
		}
		return instance;
	}
	
	public ArchaiusPropsManager() {
		try {
			
			System.setProperty("archaius.configurationSource.additionalUrls", 
					"file:///" + new File(".").getCanonicalPath() + "/aditional-resources/aditional-configs.properties");
			
			PolledConfigurationSource source = new URLConfigurationSource();
			AbstractPollingScheduler scheduler = new FixedDelayPollingScheduler(1000,1000,true);
			ConfigurationManager.install(new DynamicConfiguration(source, scheduler));
			
			DynamicStringProperty prop = DynamicPropertyFactory.getInstance().getStringProperty("netflixoss.archaius.props.createdBy", "");
			prop.addCallback(new Runnable() {
				@Override
				public void run() {
					System.out.println("polledCnfgiration: OMG my property just changed: " + DynamicPropertyFactory.getInstance().getStringProperty("netflixoss.archaius.props.createdBy", ""));
				}
			});
			System.out.println("polledCnfgiration: My Prop is:" + prop.get());
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String getCreator() {
		return DynamicPropertyFactory.getInstance().getStringProperty("netflixoss.archaius.props.createdBy", "").get();
	}
	
}

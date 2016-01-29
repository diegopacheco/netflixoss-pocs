package com.github.diegopacheco.netflixpocs.archaius;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.commons.configuration.MapConfiguration;

import com.netflix.config.ConfigurationManager;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;

public class SimpleMapConfigArchaiusConfigUsage {
	
	public static void main(String[] args) {
		mapConfiguration();
	}

	private static void mapConfiguration() {
		
		Properties props = new Properties();
		try {
			props.load( new FileInputStream(
						new File(new File(".").getCanonicalPath() + "/src/main/resources/my-configs.properties")));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		MapConfiguration configuration = new MapConfiguration(props);
		ConfigurationManager.install(configuration);
		DynamicStringProperty prop = DynamicPropertyFactory.getInstance().getStringProperty("netflix.poc.a", "");
		prop.addCallback(new Runnable() {
			@Override
			public void run() {
				System.out.println("mapConfig: OMG my property just changed");
			}
		});
		System.out.println("mapConfig: My Prop is:" + prop.get());
		
		ConfigurationManager.getConfigInstance().setProperty("netflix.poc.a", "10");
	}
	
	
	
}

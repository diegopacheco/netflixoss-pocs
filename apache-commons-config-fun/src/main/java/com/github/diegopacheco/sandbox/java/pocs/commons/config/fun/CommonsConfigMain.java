package com.github.diegopacheco.sandbox.java.pocs.commons.config.fun;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;

public class CommonsConfigMain {
	public static void main(String[] args) throws Throwable {
		
		CompositeConfiguration config = new CompositeConfiguration();
		config.addConfiguration(new SystemConfiguration());
		config.addConfiguration(new PropertiesConfiguration("simple-app.properties"));
		
		System.out.println("simple.app.name: "+  config.getString("simple.app.name"));
		System.out.println("simple.app.version: " +  config.getInt("simple.app.version"));
		System.out.println("simple.app.host: "+  config.getString("simple.app.host"));
		System.out.println("simple.app.date: "+  config.getLong("simple.app.date"));
		
		System.out.println("java.home: "+  config.getString("java.home"));
		System.out.println("application.title: "+  config.getString("application.title"));
		System.out.println("xxx.yy.zzz: "+  config.getString("xxx.yy.zzz","NONE"));
		
	}
}

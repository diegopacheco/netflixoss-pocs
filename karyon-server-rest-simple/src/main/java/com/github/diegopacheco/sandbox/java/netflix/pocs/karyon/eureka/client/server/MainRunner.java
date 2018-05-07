package com.github.diegopacheco.sandbox.java.netflix.pocs.karyon.eureka.client.server;

public class MainRunner {
	public static void main(String[] args) {
		System.setProperty("java.awt.headless","true");
		System.setProperty("archaius.deployment.environment","dev");
		System.setProperty("jersey.config.server.provider.classnames","org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider");
		netflix.karyon.KaryonRunner.main(new String[]{KaryonJerseyServerApp.class.getCanonicalName()});
	}
}
package com.github.diegopacheco.sandbox.java.netflixoss.karyon.poc.jersey;

import com.github.diegopacheco.sandbox.java.netflixoss.karyon.poc.rx.RxNettyHandler;

import netflix.adminresources.resources.KaryonWebAdminModule;
import netflix.karyon.Karyon;
import netflix.karyon.KaryonBootstrapModule;
import netflix.karyon.ShutdownModule;
import netflix.karyon.archaius.ArchaiusBootstrapModule;
import netflix.karyon.eureka.KaryonEurekaModule;
import netflix.karyon.servo.KaryonServoModule;
import netflix.karyon.transport.http.health.HealthCheckEndpoint;

public class MainRunner {
	public static void main(String[] args) {
		System.setProperty("java.awt.headless","true");
		System.setProperty("archaius.deployment.environment","dev");

//      Jetty 		
//		netflix.karyon.KaryonRunner.main(new String[]{KaryonJerseyServerApp.class.getCanonicalName()});
		
//      RxNetty		
		
		HealthcheckResource healthCheckHandler = new HealthcheckResource();
        Karyon.forRequestHandler(8888, new RxNettyHandler("/healthcheck", new HealthCheckEndpoint(healthCheckHandler)),

        		new KaryonBootstrapModule(healthCheckHandler),
                new ArchaiusBootstrapModule("simplemath-netflix-oss"),
                KaryonEurekaModule.asBootstrapModule(),
                Karyon.toBootstrapModule(KaryonWebAdminModule.class),
                ShutdownModule.asBootstrapModule(),
                KaryonServoModule.asBootstrapModule()
        ).startAndWaitTillShutdown();
		
	}
}

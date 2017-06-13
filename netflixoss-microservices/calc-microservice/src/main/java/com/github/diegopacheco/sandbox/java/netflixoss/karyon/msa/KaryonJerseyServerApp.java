package com.github.diegopacheco.sandbox.java.netflixoss.karyon.msa;

import com.github.diegopacheco.sandbox.java.netflixoss.karyon.msa.KaryonJerseyServerApp.KaryonJerseyModuleImpl;
import com.google.inject.servlet.ServletModule;
import com.netflix.governator.annotations.Modules;
import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import com.scmspain.karyon.hystrixstreamendpoint.module.HystrixStreamEndPointModule;

import netflix.adminresources.resources.KaryonWebAdminModule;
import netflix.karyon.KaryonBootstrap;
import netflix.karyon.archaius.ArchaiusBootstrap;
import netflix.karyon.eureka.KaryonEurekaModule;
import netflix.karyon.jersey.blocking.KaryonJerseyModule;
import netflix.karyon.servo.KaryonServoModule;

@ArchaiusBootstrap
@KaryonBootstrap(name = "calc-microservice", healthcheck = HealthcheckResource.class)
@Modules(include = {
        ShutdownModule.class,
        KaryonWebAdminModule.class,
        KaryonServoModule.class,
        KaryonEurekaModule.class,
        GuiceBindingsModule.class,
        KaryonJerseyServerApp.HystrixServletModule.class,
        KaryonJerseyServerApp.KaryonJerseyModuleImpl.class
})
public interface KaryonJerseyServerApp {
	class KaryonJerseyModuleImpl extends KaryonJerseyModule {
        @Override
        protected void configureServer() {
    		bind(AuthenticationService.class).to(AuthenticationServiceImpl.class);
    		
    		interceptorSupport().forUri("/*").intercept(LoggingInterceptor.class);
            interceptorSupport().forUri("/*").interceptIn(AuthInterceptor.class);
            
            server().port(6005).threadPoolSize(400);		
        }
	}	
	class HystrixServletModule extends ServletModule{
	     @Override
	     protected void configureServlets() {
	    	 serve("/hd/hystrix.stream").with(HystrixMetricsStreamServlet.class);
	     }
	}
}

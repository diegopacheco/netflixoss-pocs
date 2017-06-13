package com.github.diegopacheco.sandbox.java.netflixoss.karyon.msa;

import com.github.diegopacheco.sandbox.java.netflixoss.karyon.msa.KaryonJerseyServerApp.KaryonJerseyModuleImpl;
import com.github.diegopacheco.sandbox.java.netflixoss.karyon.rest.CalcService;
import com.github.diegopacheco.sandbox.java.netflixoss.karyon.ribbon.RibbonMathClient;
import com.netflix.governator.annotations.Modules;

import netflix.adminresources.resources.KaryonWebAdminModule;
import netflix.karyon.KaryonBootstrap;
import netflix.karyon.archaius.ArchaiusBootstrap;
import netflix.karyon.eureka.KaryonEurekaModule;
import netflix.karyon.jersey.blocking.JerseyBasedRouter;
import netflix.karyon.servo.KaryonServoModule;

@ArchaiusBootstrap
@KaryonBootstrap(name = "calc-microservice", healthcheck = HealthcheckResource.class)
@Modules(include = {
        ShutdownModule.class,
        KaryonWebAdminModule.class,
        KaryonServoModule.class,
        KaryonJerseyModuleImpl.class,
        KaryonEurekaModule.class 
})
public interface KaryonJerseyServerApp {
	 class KaryonJerseyModuleImpl extends KaryonHystrixModule {
		
		@javax.inject.Inject
	    public KaryonJerseyModuleImpl(JerseyBasedRouter jerseyRouter) {
			super(jerseyRouter);
		}

		@Override
	    protected void configureServer() {
	    	
	    	bind(RibbonMathClient.class);
	    	bind(CalcService.class);
	    	
	        bind(AuthenticationService.class).to(AuthenticationServiceImpl.class);
	        interceptorSupport().forUri("/*").intercept(LoggingInterceptor.class);
	        interceptorSupport().forUri("/*").interceptIn(AuthInterceptor.class);
	        server().port(6005).threadPoolSize(400);
	    }
	 }
}

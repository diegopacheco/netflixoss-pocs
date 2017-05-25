package com.github.diegopacheco.sandbox.java.netflixoss.msa;

import com.github.diegopacheco.sandbox.java.netflixoss.msa.KaryonJerseyServerApp.KaryonJerseyModuleImpl;
import com.netflix.governator.annotations.Modules;

import netflix.adminresources.resources.KaryonWebAdminModule;
import netflix.karyon.KaryonBootstrap;
import netflix.karyon.ShutdownModule;
import netflix.karyon.archaius.ArchaiusBootstrap;
import netflix.karyon.jersey.blocking.KaryonJerseyModule;
import netflix.karyon.servo.KaryonServoModule;

@ArchaiusBootstrap
@KaryonBootstrap(name = "karyon-microservice-xray", healthcheck = HealthcheckResource.class)
@Modules(include = {
        ShutdownModule.class,
        KaryonWebAdminModule.class,
        KaryonServoModule.class,
        KaryonJerseyModuleImpl.class,
        //KaryonEurekaModule.class 
})
public interface KaryonJerseyServerApp {
	 class KaryonJerseyModuleImpl extends KaryonJerseyModule {
	        @Override
	        protected void configureServer() {
	            bind(AuthenticationService.class).to(AuthenticationServiceImpl.class);
	            
	            interceptorSupport().forUri("/*").intercept(LoggingInterceptor.class);
	            interceptorSupport().forUri("/*").interceptIn(AuthInterceptor.class);
	            
	            interceptorSupport().forUri("/cache/*").intercept(AWSXrayInterceptor.class);
	            
	            server().port(6002).threadPoolSize(400);
	            
	        }
	 }
}

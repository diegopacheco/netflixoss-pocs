package com.github.diegopacheco.sandbox.java.netflixoss.karyon.msa;

import com.netflix.governator.annotations.Modules;

import netflix.adminresources.resources.KaryonWebAdminModule;
import netflix.karyon.KaryonBootstrap;
import netflix.karyon.archaius.ArchaiusBootstrap;
import netflix.karyon.eureka.KaryonEurekaModule;
import netflix.karyon.servo.KaryonServoModule;

@ArchaiusBootstrap
@KaryonBootstrap(name = "calc-microservice", healthcheck = HealthcheckResource.class)
@Modules(include = { 
		ShutdownModule.class, 
		KaryonWebAdminModule.class, 
		KaryonServoModule.class,
		KaryonEurekaModule.class, 
		GuiceBindingsModule.class, 
		KaryonJerseyServerApp.KaryonJerseyModuleImpl.class 
})
public interface KaryonJerseyServerApp {
	class KaryonJerseyModuleImpl extends KaryonHystrixJerseyModule {
		@Override
		protected void configureServer() {
			bind(AuthenticationService.class).to(AuthenticationServiceImpl.class);

			interceptorSupport().forUri("/*").intercept(LoggingInterceptor.class);
			interceptorSupport().forUri("/*").interceptIn(AuthInterceptor.class);

			server().port(6005).threadPoolSize(400);
		}
	}
}

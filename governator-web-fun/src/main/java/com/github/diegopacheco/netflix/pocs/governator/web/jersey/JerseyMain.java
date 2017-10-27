package com.github.diegopacheco.netflix.pocs.governator.web.jersey;

import java.util.Map;
import java.util.function.UnaryOperator;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.util.Modules;
import com.netflix.governator.InjectorBuilder;
import com.netflix.governator.LifecycleInjector;
import com.netflix.governator.ShutdownHookModule;
import com.netflix.governator.guice.jersey.GovernatorJerseySupportModule;
import com.netflix.governator.guice.jersey.GovernatorServletContainer;
import com.netflix.governator.guice.jetty.DefaultJettyConfig;
import com.netflix.governator.guice.jetty.JettyConfig;
import com.netflix.governator.guice.jetty.JettyModule;
import com.netflix.governator.providers.Advises;
import com.sun.jersey.api.core.DefaultResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.guice.JerseyServletModule;

public class JerseyMain {
	
	@Path("/")
	@Singleton
	public static class SimpleResource {
		public SimpleResource() {}
		@GET
		public String work() {
			return "It Works";
		}
	}

	public static void main(String[] args) {

		LifecycleInjector injector = InjectorBuilder
				.fromModules(
						new ShutdownHookModule(), 
						Modules.override(new JettyModule())
							.with(new AbstractModule() {
									protected void configure() {}
									@Provides
									JettyConfig getConfig() {
										return new DefaultJettyConfig().setPort(9090);
									}
						}), 
						new GovernatorJerseySupportModule(), 
			               new JerseyServletModule() {
		                    @Override
		                    protected void configureServlets() {
		                        serve("/*").with(GovernatorServletContainer.class);
		                    }
		                    @Advises
		                    @Singleton
		                    @Named("governator")
		                    UnaryOperator<DefaultResourceConfig> getResourceConfig() {
		                        return config -> {
		                            Map<String, Object> props = config.getProperties();
		                            props.put(ResourceConfig.FEATURE_DISABLE_WADL, "false");
		                            config.getClasses().add(SimpleResource.class);
		                            return config;
		                        };
		                    }
		                }
				).createInjector();
		System.out.println(injector);

	}

}

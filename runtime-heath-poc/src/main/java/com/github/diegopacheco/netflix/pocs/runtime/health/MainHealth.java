package com.github.diegopacheco.netflix.pocs.runtime.health;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import java.util.function.UnaryOperator;

import javax.inject.Named;
import javax.inject.Singleton;

import com.github.diegopacheco.netflix.pocs.runtime.archaius.DefaultPropertyFactory;
import com.github.diegopacheco.netflix.pocs.runtime.eureka.SimpleDiscoveryClientOptionalArgs;
import com.github.diegopacheco.netflix.pocs.runtime.rest.SimpleResource;
import com.google.common.collect.Maps;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.util.Modules;
import com.netflix.archaius.DefaultDecoder;
import com.netflix.archaius.api.Config;
import com.netflix.archaius.api.Decoder;
import com.netflix.archaius.api.PropertyFactory;
import com.netflix.archaius.api.exceptions.ConfigException;
import com.netflix.archaius.config.DefaultCompositeConfig;
import com.netflix.archaius.config.MapConfig;
import com.netflix.archaius.config.SystemConfig;
import com.netflix.discovery.AbstractDiscoveryClientOptionalArgs;
import com.netflix.governator.InjectorBuilder;
import com.netflix.governator.ShutdownHookModule;
import com.netflix.governator.guice.jersey.GovernatorJerseySupportModule;
import com.netflix.governator.guice.jersey.GovernatorServletContainer;
import com.netflix.governator.guice.jetty.DefaultJettyConfig;
import com.netflix.governator.guice.jetty.JettyConfig;
import com.netflix.governator.guice.jetty.JettyModule;
import com.netflix.governator.providers.Advises;
import com.netflix.runtime.health.eureka.EurekaHealthStatusBridgeModule;
import com.netflix.runtime.health.guice.HealthModule;
import com.sun.jersey.api.core.DefaultResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.guice.JerseyServletModule;

public class MainHealth {

	public static void main(String[] args) {

		InjectorBuilder.fromModules(new HealthModule() {

			protected void configureHealth() {
				bindAdditionalHealthIndicator().to(MyHealthIndicator.class);

				bind(Decoder.class).to(DefaultDecoder.class);
				bind(PropertyFactory.class).to(DefaultPropertyFactory.class);
				
				bind(AbstractDiscoveryClientOptionalArgs.class).to(SimpleDiscoveryClientOptionalArgs.class);
			}

			@Provides
			@Singleton
			Config getConfigs() {
				Map<String, String> data = Maps.newConcurrentMap();
				Map<String, String> env = System.getenv();

				for (String key : env.keySet()) {
					String newKey = key.replaceAll("_", ".");
					data.put(newKey, env.get(key));
				}

				Properties properties = new Properties();
				URL url = this.getClass().getClassLoader().getResource("app.properties");
				if (url != null) {
					try {
						properties.load(url.openStream());
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					System.out.println("No app.properties. Ignore!");
				}

				DefaultCompositeConfig config = new DefaultCompositeConfig();
				try {
					config.addConfig("system", new SystemConfig());
					config.addConfig("envs", new MapConfig(data));
					config.addConfig("props", new MapConfig(properties));

				} catch (ConfigException e) {
					e.printStackTrace();
				}
				return config;
			}
		}, new ShutdownHookModule(), Modules.override(new JettyModule()).with(new AbstractModule() {
			protected void configure() {
			}

			@Provides
			JettyConfig getConfig() {
				return new DefaultJettyConfig().setPort(9090);
			}
		}), new GovernatorJerseySupportModule(), new JerseyServletModule() {
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
					config.getClasses().add(HealthCheckResource.class);
					return config;
				};
			}
		}
				
		//, new EurekaHealthStatusBridgeModule()
		
		).createInjector();

	}

}

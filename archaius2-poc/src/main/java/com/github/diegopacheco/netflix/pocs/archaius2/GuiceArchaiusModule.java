package com.github.diegopacheco.netflix.pocs.archaius2;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

import javax.inject.Singleton;

import com.google.common.collect.Maps;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.netflix.archaius.ConfigProxyFactory;
import com.netflix.archaius.DefaultDecoder;
import com.netflix.archaius.api.Config;
import com.netflix.archaius.api.Decoder;
import com.netflix.archaius.api.PropertyFactory;
import com.netflix.archaius.api.exceptions.ConfigException;
import com.netflix.archaius.config.DefaultCompositeConfig;
import com.netflix.archaius.config.MapConfig;
import com.netflix.archaius.config.SystemConfig;

public class GuiceArchaiusModule extends AbstractModule {

	@Override
	protected void configure() {

		bind(SimpleService.class).asEagerSingleton();

		/* Archaius bindings */
		bind(Decoder.class).to(DefaultDecoder.class);
		bind(PropertyFactory.class).to(DefaultPropertyFactory.class);
	}

	@Provides
	@Singleton
	Config getConfigs() {
		Map<String, String> data = Maps.newConcurrentMap();
		Map<String, String> env = System.getenv();
		
		for (String key : env.keySet()) {
			String newKey = key.replaceAll("_", ".");
			data.put("app." + newKey, env.get(key));
		}
		
		
        Properties properties = new Properties();
        URL url = this.getClass().getClassLoader().getResource("app.properties");
        if (url != null) {
            try {
                properties.load(url.openStream());
            } 
            catch (IOException e) {
            	e.printStackTrace();
            }
        } else {
            System.out.println("No app.properties. Ignore!");
        }
		
		DefaultCompositeConfig config = new DefaultCompositeConfig();
		try {
			config.addConfig("system", new SystemConfig());
			config.addConfig("envs",  new MapConfig(data));
			config.addConfig("props", new MapConfig(properties));
			
		} catch (ConfigException e) {
			e.printStackTrace();
		}
		return config;
	}

	@Provides
	@Singleton
	AppConfig getAppConfig(ConfigProxyFactory factory) {
		return factory.newProxy(AppConfig.class);
	}

}

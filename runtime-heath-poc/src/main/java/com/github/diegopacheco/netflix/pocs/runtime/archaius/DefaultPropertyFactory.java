package com.github.diegopacheco.netflix.pocs.runtime.archaius;

import javax.inject.Inject;

import com.netflix.archaius.api.Config;

public class DefaultPropertyFactory extends com.netflix.archaius.DefaultPropertyFactory {
	
	@Inject
	public DefaultPropertyFactory(Config config) {
		super(config);
	}
	
}

package com.github.diegopacheco.sandbox.java.netflixoss.karyon.poc.hystrix;

import java.util.Date;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;

public class DateHystrixCommand extends HystrixCommand<Date> {

	private final String name;

	public DateHystrixCommand(String name) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("DateHystrixCommand"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(3000)));
        this.name = name;
	}

	@Override
	protected Date run() {
		return new Date();
	}
	
	public String getName() {
		return name;
	}

}

package com.github.diegopacheco.sandbox.java.netflixoss.karyon.msa;

import javax.annotation.PostConstruct;

import com.netflix.hystrix.contrib.rxnetty.metricsstream.HystrixMetricsStreamHandler;

import io.netty.buffer.ByteBuf;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import io.reactivex.netty.protocol.http.server.HttpServerResponse;
import netflix.karyon.jersey.blocking.JerseyBasedRouter;
import rx.Observable;

public class JerseyHystrixBasedRouter extends JerseyBasedRouter{
	
	private HystrixMetricsStreamHandler<ByteBuf, ByteBuf> hystrixStreamHandler;
	
	@PostConstruct
	public void init(){
		this.hystrixStreamHandler =  new HystrixMetricsStreamHandler<ByteBuf, ByteBuf>(this);
	}

	@Override
	public Observable<Void> handle(HttpServerRequest<ByteBuf> request, HttpServerResponse<ByteBuf> response) {
		return hystrixStreamHandler.handle(request, response);
	}
	
}

package com.github.diegopacheco.sandbox.java.netflixoss.karyon.msa;

import com.google.inject.Inject;
import com.netflix.hystrix.contrib.rxnetty.metricsstream.HystrixMetricsStreamHandler;

import io.netty.buffer.ByteBuf;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import io.reactivex.netty.protocol.http.server.HttpServerResponse;
import io.reactivex.netty.protocol.http.server.RequestHandler;
import netflix.karyon.jersey.blocking.JerseyBasedRouter;
import netflix.karyon.jersey.blocking.KaryonJerseyModule;
import rx.Observable;

public class KaryonHystrixModule extends KaryonJerseyModule implements RequestHandler<ByteBuf, ByteBuf> {

	private final HystrixMetricsStreamHandler<ByteBuf, ByteBuf> hystrixStreamHandler;

	@Inject
	public KaryonHystrixModule(JerseyBasedRouter jerseyRouter) {
		this.hystrixStreamHandler = new HystrixMetricsStreamHandler<ByteBuf, ByteBuf>(jerseyRouter);
	}

	@Override
	public Observable<Void> handle(HttpServerRequest<ByteBuf> request, HttpServerResponse<ByteBuf> response) {
		return hystrixStreamHandler.handle(request, response);
	}

	@Override
	protected void configureServer() {}
}
package com.github.diegopacheco.sandbox.java.netflixoss.karyon.msa;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.google.inject.Injector;
import com.netflix.hystrix.contrib.rxnetty.metricsstream.HystrixMetricsStreamHandler;

import io.netty.buffer.ByteBuf;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import io.reactivex.netty.protocol.http.server.HttpServerResponse;
import netflix.karyon.jersey.blocking.JerseyBasedRouter;
import rx.Observable;

public class JerseyHystrixBasedRouter extends JerseyBasedRouter {

	private HystrixMetricsStreamHandler<ByteBuf, ByteBuf> hystrixStreamHandler;

	@Inject
	public JerseyHystrixBasedRouter(Injector injector) {
		super(injector);
	}

	@PostConstruct
	public void init() {
		this.hystrixStreamHandler = new HystrixMetricsStreamHandler<ByteBuf, ByteBuf>(this);
	}

	@Override
	public Observable<Void> handle(HttpServerRequest<ByteBuf> request, HttpServerResponse<ByteBuf> response) {
		if (request.getPath().startsWith("/hystrix.stream")) {
			return hystrixStreamHandler.handle(request, response);
		}
		return super.handle(request, response);
	}

}

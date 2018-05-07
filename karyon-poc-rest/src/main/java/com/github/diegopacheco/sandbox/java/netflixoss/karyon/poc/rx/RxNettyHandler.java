package com.github.diegopacheco.sandbox.java.netflixoss.karyon.poc.rx;

import com.github.diegopacheco.sandbox.java.netflixoss.karyon.poc.jersey.SimpleMathResource;
import com.netflix.hystrix.contrib.rxnetty.metricsstream.HystrixMetricsStreamHandler;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import io.reactivex.netty.protocol.http.server.HttpServerResponse;
import io.reactivex.netty.protocol.http.server.RequestHandler;
import netflix.karyon.transport.http.health.HealthCheckEndpoint;
import rx.Observable;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class RxNettyHandler implements RequestHandler<ByteBuf, ByteBuf> {

	private final String healthCheckUri;
	private final HealthCheckEndpoint healthCheckEndpoint;

	public RxNettyHandler(String healthCheckUri, HealthCheckEndpoint healthCheckEndpoint) {
		this.healthCheckUri = healthCheckUri;
		this.healthCheckEndpoint = healthCheckEndpoint;
	}

	private boolean validateBadRequest(HttpServerRequest<ByteBuf> request,HttpServerResponse<ByteBuf> response){
	
		int prefixLength = "/math/sum/".length();
		String parameters = request.getPath().substring(prefixLength);
		String v1 = parameters.split("/")[0];
		String v2 = parameters.split("/")[1];
	
		if (v1.isEmpty() || v2.isEmpty()) {
			return false;
		}else{
			return true;
		}
	}
	
	
	@Override
	public Observable<Void> handle(HttpServerRequest<ByteBuf> request, HttpServerResponse<ByteBuf> response) {
		
		 if (request.getUri().startsWith("/hystrix.stream")) 
			 return new HystrixMetricsStreamHandler(healthCheckEndpoint).handle(request, response);
		
		else if (request.getUri().contains("/div")) {
				
				if(!validateBadRequest(request,response)){
					response.setStatus(HttpResponseStatus.BAD_REQUEST);
					return response.writeStringAndFlush("{\"Error\":\"Please provide a v1 and v2 values to say hello. The URI should be /math/v1/v2\"}");
				}

				int prefixLength = "/math/div/".length();
				String parameters = request.getPath().substring(prefixLength);
				String v1 = parameters.split("/")[0];
				String v2 = parameters.split("/")[1];
				
				String result = new SimpleMathResource().div(new Double(v1), new Double(v2));
				return response.writeStringAndFlush("{\"Message\":\"" + result + "\"}");
					
		 }else if (request.getUri().startsWith(healthCheckUri)) {
		
			return healthCheckEndpoint.handle(request, response);
		
		} else if (request.getUri().startsWith("/math")) {
			
			if(!validateBadRequest(request,response)){
				response.setStatus(HttpResponseStatus.BAD_REQUEST);
				return response.writeStringAndFlush("{\"Error\":\"Please provide a v1 and v2 values to say hello. The URI should be /math/v1/v2\"}");
			}

			int prefixLength = "/math/sum/".length();
			String parameters = request.getPath().substring(prefixLength);
			String v1 = parameters.split("/")[0];
			String v2 = parameters.split("/")[1];
			
			String result = new SimpleMathResource().sumRx(new Double(v1), new Double(v2));
			return response.writeStringAndFlush("{\"Message\":\"" + result + "\"}");
				
		} else if (request.getUri().startsWith("/hi")) {
			
			return response.writeStringAndFlush("{\"Message\":\"Hello newbee from Netflix OSS\"}");
			
		} else {
			response.setStatus(HttpResponseStatus.NOT_FOUND);
			return response.close();
		}
	}

}

package com.github.diegopacheco.sandbox.java.netflixoss.msa;

import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.AWSXRayRecorderBuilder;
import com.amazonaws.xray.plugins.EC2Plugin;
import com.amazonaws.xray.strategy.sampling.LocalizedSamplingStrategy;

import io.netty.buffer.ByteBuf;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import io.reactivex.netty.protocol.http.server.HttpServerResponse;
import netflix.karyon.transport.interceptor.DuplexInterceptor;
import rx.Observable;

public class AWSXrayInterceptor implements DuplexInterceptor<HttpServerRequest<ByteBuf>, HttpServerResponse<ByteBuf>> {

    private static final Logger logger = LoggerFactory.getLogger(AWSXrayInterceptor.class);
    
    public AWSXrayInterceptor() {}

	static{
		AWSXRayRecorderBuilder builder = AWSXRayRecorderBuilder.standard().withPlugin(new EC2Plugin());
        URL ruleFile = AWSXrayInterceptor.class.getResource("/sampling-rules.json");
        builder.withSamplingStrategy(new LocalizedSamplingStrategy(ruleFile));
        AWSXRay.setGlobalRecorder(builder.build());
        
        com.amazonaws.xray.entities.Segment segment = AWSXRay.beginSegment("Cache.test");
        System.out.println("Testing segments...");
        segment.end();
	}
    
    @Override
    public Observable<Void> in(HttpServerRequest<ByteBuf> request, HttpServerResponse<ByteBuf> response) {
    	AWSXRay.beginSegment("cache");
    	logger.info("Logging interceptor with AWS XRAY inboud.");
    	AWSXRay.endSegment();
        return Observable.empty();
    }

    @Override
    public Observable<Void> out(HttpServerResponse<ByteBuf> response) {
    	AWSXRay.beginSegment("cache");
    	logger.info("Logging interceptor with AWS XRAY Outbound.");
    	AWSXRay.endSegment();
        return Observable.empty();
    }
}
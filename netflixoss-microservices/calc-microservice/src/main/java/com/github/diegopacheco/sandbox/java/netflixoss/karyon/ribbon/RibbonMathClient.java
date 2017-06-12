package com.github.diegopacheco.sandbox.java.netflixoss.karyon.ribbon;

import java.nio.charset.Charset;

import com.google.inject.Injector;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.governator.guice.LifecycleInjector;
import com.netflix.governator.guice.LifecycleInjectorBuilder;
import com.netflix.ribbon.ClientOptions;
import com.netflix.ribbon.Ribbon;
import com.netflix.ribbon.RibbonResponse;
import com.netflix.ribbon.http.HttpRequestTemplate;
import com.netflix.ribbon.http.HttpResourceGroup;

import io.netty.buffer.ByteBuf;
import rx.Observable;

@SuppressWarnings("unchecked")
public class RibbonMathClient { 
	
	private String callService(String microservice,String path,Double a,Double b){
			
		LifecycleInjectorBuilder builder = LifecycleInjector.builder();
		Injector injector = builder.build().createInjector();
		EurekaClient client = injector.getInstance(EurekaClient.class);
		
		InstanceInfo info = client.getApplication(microservice).getInstances().get(0);
		String serverPort =  "http://" + info.getVIPAddress() + ":" + info.getPort();
		
		HttpResourceGroup httpRG = Ribbon.createHttpResourceGroup("apiGroup",
	            ClientOptions.create()
                .withMaxAutoRetriesNextServer(1)
                .withConfigurationBasedServerList(serverPort));
		
		HttpRequestTemplate<ByteBuf> apiTemplate = httpRG.newTemplateBuilder("apiCall",ByteBuf.class)
		            .withMethod("GET")
		            .withUriTemplate(path + a + "/" + b)
		            .withFallbackProvider(new DefaultFallback())
		            .withResponseValidator(new SimpleResponseValidator())
		            .build();
		
		RibbonResponse<ByteBuf> result = apiTemplate.requestBuilder()
									 .withHeader("client", "calc-microservice")
					                 .build()
					                 .withMetadata().execute();
		ByteBuf buf = result.content();
		String json = buf.toString(Charset.forName("UTF-8" ));
		return json;
	}
	
	public Observable<Double> sum(Double a, Double b){
		return Observable.just(new Double(callService("sum-microservice","/math/sum/",a,b)));
	}
	
	public Observable<Double> sub(Double a, Double b){
		return Observable.just(new Double(callService("sub-microservice", "/math/sub/",a,b)));
	}
	
	public Observable<Double> mul(Double a, Double b){
		return Observable.just(new Double(callService("mul-microservice" ,"/math/mul/",a,b)));
	}
	
	public Observable<Double> div(Double a, Double b){
		return Observable.just(new Double(callService("div-microservice", "/math/div/",a,b)));
	}
	
}

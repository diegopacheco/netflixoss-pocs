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
	
	private EurekaClient eurekaClient;
	
	private EurekaClient getEurekaClient(){
		if (eurekaClient==null){
			LifecycleInjectorBuilder builder = LifecycleInjector.builder();
			Injector injector = builder.build().createInjector();
			eurekaClient = injector.getInstance(EurekaClient.class);
		}
		return eurekaClient;
	}
	
	private String getServerIP(String microservice){
		try{
			InstanceInfo info = getEurekaClient().getApplication(microservice.toUpperCase()).getInstances().get(0);
			String serverPort =  "http://" + info.getVIPAddress() + ":" + info.getPort();
			return serverPort;
		}catch(Exception e){
			throw new RuntimeException("Could not get Microservice IP:PORT. EX: " + e);
		}
	}
	
	private String callService(String microservice,String path,Double a,Double b){

		HttpResourceGroup httpRG = Ribbon.createHttpResourceGroup("apiGroup",
	            ClientOptions.create()
                .withMaxAutoRetriesNextServer(1)
                .withConfigurationBasedServerList(getServerIP(microservice)));
		
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
		return Observable.just(new Double(callService("sum-service","/math/sum/",a,b)));
	}
	
	public Observable<Double> sub(Double a, Double b){
		return Observable.just(new Double(callService("sub-service", "/math/sub/",a,b)));
	}
	
	public Observable<Double> mul(Double a, Double b){
		return Observable.just(new Double(callService("mul-service" ,"/math/mul/",a,b)));
	}
	
	public Observable<Double> div(Double a, Double b){
		return Observable.just(new Double(callService("div-service", "/math/div/",a,b)));
	}
	
}

package com.github.diegopacheco.sandbox.java.netflix.ribbon.eureka.client;

import java.nio.charset.Charset;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Injector;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.config.ConfigurationManager;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.providers.DefaultEurekaClientConfigProvider;
import com.netflix.governator.guice.LifecycleInjector;
import com.netflix.governator.guice.LifecycleInjectorBuilder;
import com.netflix.ribbon.ClientOptions;
import com.netflix.ribbon.Ribbon;
import com.netflix.ribbon.RibbonResponse;
import com.netflix.ribbon.http.HttpRequestTemplate;
import com.netflix.ribbon.http.HttpResourceGroup;

import io.netty.buffer.ByteBuf;

public class RibbonClientUsingEureka {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		
		// http://localhost:6002/weather/now/gravatai
		
		System.out.println("apiCall.ribbon.MaxAutoRetriesNextServer: " +  ConfigurationManager.getConfigInstance().getProperty("apiCall.ribbon.MaxAutoRetriesNextServer"));
		System.out.println("apiCall.ribbon.ConnectTimeout: " +  ConfigurationManager.getConfigInstance().getProperty("apiCall.ribbon.ConnectTimeout"));
		System.out.println("apiCall.ribbon.ReadTimeout: " +  ConfigurationManager.getConfigInstance().getProperty("apiCall.ribbon.ReadTimeout"));
		
		System.out.println("Using eureka: " + new DefaultEurekaClientConfigProvider().get().getEurekaServerServiceUrls("default"));
		
		LifecycleInjectorBuilder builder = LifecycleInjector.builder();
		Injector injector = builder.build().createInjector();
		EurekaClient client = injector.getInstance(EurekaClient.class);
		
		InstanceInfo info = client.getApplication("WEATHER-SERVICE").getInstances().get(0);
		String server =  "http://" + info.getVIPAddress() + ":" + info.getPort();
		System.out.println("Eureka Discovery client using: " + server);
		
		HttpResourceGroup httpRG = Ribbon.createHttpResourceGroup("apiCall",
	            ClientOptions.create()
                .withMaxAutoRetriesNextServer(1)
                .withConfigurationBasedServerList(server)
                );
		
		HttpRequestTemplate<ByteBuf> apiTemplate = httpRG.newTemplateBuilder("apiCall",ByteBuf.class)
		            .withMethod("GET")
		            .withUriTemplate("/weather/now/gravatai")
		            .withFallbackProvider(new ApiFallbackHandler())
		            .withResponseValidator(new ApiResponseValidator())
		            .build();
		
		RibbonResponse<ByteBuf> result = apiTemplate.requestBuilder()
					                 .build()
					                 .withMetadata().execute();
		
		ByteBuf buf = result.content();
		String json = buf.toString(Charset.forName("UTF-8" ));
		System.out.println("Result in Json: " + json);
		
		Gson gson = new Gson();
		Map<String, String> mapResult = gson.fromJson(json, new TypeToken<Map<String, String>>() {}.getType()); 
		
		System.out.println("Result description: " + mapResult.get("description"));
		System.out.println("Result country: " + mapResult.get("country"));
		System.out.println("Result temp: " + mapResult.get("temp"));
		
		System.exit(0);
		
	}
}


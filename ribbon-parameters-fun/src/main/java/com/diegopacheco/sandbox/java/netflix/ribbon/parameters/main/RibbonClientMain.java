package com.diegopacheco.sandbox.java.netflix.ribbon.parameters.main;

import java.nio.charset.Charset;

import com.netflix.config.ConfigurationManager;
import com.netflix.ribbon.ClientOptions;
import com.netflix.ribbon.Ribbon;
import com.netflix.ribbon.RibbonResponse;
import com.netflix.ribbon.http.HttpRequestTemplate;
import com.netflix.ribbon.http.HttpResourceGroup;

import io.netty.buffer.ByteBuf;

//
// http://api.openweathermap.org/data/2.5/weather?q=Sunnyvale
//
public class RibbonClientMain {
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception{
		
		System.out.println("apiCall.ribbon.MaxAutoRetriesNextServer: " +  ConfigurationManager.getConfigInstance().getProperty("apiCall.ribbon.MaxAutoRetriesNextServer"));
		System.out.println("apiCall.ribbon.ConnectTimeout: " +  ConfigurationManager.getConfigInstance().getProperty("apiCall.ribbon.ConnectTimeout"));
		System.out.println("apiCall.ribbon.ReadTimeout: " +  ConfigurationManager.getConfigInstance().getProperty("apiCall.ribbon.ReadTimeout"));
		
		HttpResourceGroup httpRG = Ribbon.createHttpResourceGroup("apiCall",
	            ClientOptions.create()
                .withMaxAutoRetriesNextServer(1)
                .withConfigurationBasedServerList("http://api.openweathermap.org.null"));
		
		HttpRequestTemplate<ByteBuf> apiTemplate = httpRG.newTemplateBuilder("apiCall",ByteBuf.class)
		            .withMethod("GET")
		            .withUriTemplate("/data/2.5/weather?q=Sunnyvale")
		            .withFallbackProvider(new ApiFallbackHandler())
		            .withResponseValidator(new ApiResponseValidator())
		            .build();
		
		RibbonResponse<ByteBuf> result = apiTemplate.requestBuilder()
					                 .build()
					                 .withMetadata().execute();
		
		ByteBuf buf = result.content();
		String json = buf.toString(Charset.forName("UTF-8" ));
		System.out.println("Result in Json: " + json);
		
		System.exit(0);
	}
	
}

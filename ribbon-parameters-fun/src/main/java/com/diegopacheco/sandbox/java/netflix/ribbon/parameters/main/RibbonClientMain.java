package com.diegopacheco.sandbox.java.netflix.ribbon.parameters.main;

import java.nio.charset.Charset;

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
	
	static{
		System.setProperty("ribbon.listOfServers","http://api.openweathermap.org/data/2.5/");
	}
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception{
		
		HttpResourceGroup httpRG = Ribbon.createHttpResourceGroup("apiCall",
	            ClientOptions.create()
                .withMaxAutoRetriesNextServer(1)
                .withConfigurationBasedServerList("http://api.openweathermap.org/data/2.5/"));
		
		HttpRequestTemplate<ByteBuf> apiTemplate = httpRG.newTemplateBuilder("apiCall",ByteBuf.class)
		            .withMethod("GET")
		            .withUriTemplate("weather?q=Sunnyvale")
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

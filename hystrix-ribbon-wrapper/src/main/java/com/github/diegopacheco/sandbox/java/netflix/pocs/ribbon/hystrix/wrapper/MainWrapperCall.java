package com.github.diegopacheco.sandbox.java.netflix.pocs.ribbon.hystrix.wrapper;

import java.nio.charset.Charset;

import com.github.diegopacheco.sandbox.java.netflix.pocs.ribbon.hystrix.ribbon.ApiFallbackHandler;
import com.github.diegopacheco.sandbox.java.netflix.pocs.ribbon.hystrix.ribbon.ApiResponseValidator;
import com.netflix.ribbon.ClientOptions;
import com.netflix.ribbon.Ribbon;
import com.netflix.ribbon.RibbonResponse;
import com.netflix.ribbon.http.HttpRequestTemplate;
import com.netflix.ribbon.http.HttpResourceGroup;

import io.netty.buffer.ByteBuf;

@SuppressWarnings("unchecked")
public class MainWrapperCall {
	
	public static void main(String[] args) {
		
		HttpResourceGroup httpRG = Ribbon.createHttpResourceGroup("apiCall2",
	            ClientOptions.create()
                .withMaxAutoRetriesNextServer(1)
                .withConfigurationBasedServerList("http://api.openweathermap.org"));
		
		HttpRequestTemplate<ByteBuf> apiTemplate = httpRG.newTemplateBuilder("apiCall2",ByteBuf.class)
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
		
	}
}

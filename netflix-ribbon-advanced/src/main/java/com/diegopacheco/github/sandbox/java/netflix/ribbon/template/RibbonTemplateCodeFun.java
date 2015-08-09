package com.diegopacheco.github.sandbox.java.netflix.ribbon.template;

import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

import rx.Observable;

import com.netflix.config.ConfigurationManager;
import com.netflix.ribbon.ClientOptions;
import com.netflix.ribbon.Ribbon;
import com.netflix.ribbon.RibbonResponse;
import com.netflix.ribbon.http.HttpRequestTemplate;
import com.netflix.ribbon.http.HttpResourceGroup;

@SuppressWarnings("unchecked")
public class RibbonTemplateCodeFun {
	
	static{
		System.setProperty("ribbon.listOfServers","127.0.0.1:7001");
	}
	
	public static void main(String[] args) throws Exception{
		
		HttpResourceGroup httpRG = Ribbon.createHttpResourceGroup("apiGroup",
	            ClientOptions.create()
                .withMaxAutoRetriesNextServer(1)
                .withConfigurationBasedServerList("localhost:7001"));
		
		HttpRequestTemplate<ByteBuf> apiTemplate = httpRG.newTemplateBuilder("apiCall",ByteBuf.class)
		            .withMethod("GET")
		            .withUriTemplate("api")
		            .withFallbackProvider(new ApiFallbackHandler())
		            .withResponseValidator(new ApiResponseValidator())
		            .withHeader("MY_HEADER_ROCKS", "1234567891011213")
		            .build();
		
		System.out.println("Template: " + apiTemplate);
		
		RibbonResponse<ByteBuf> result = apiTemplate.requestBuilder()
					                 .build()
					                 .withMetadata().execute();
		System.out.println("Result: " + result);
		
		ByteBuf buf = result.content();
		String json = buf.toString(Charset.forName("UTF-8" ));
		System.out.println("Result in Json: " + json);
		
	}
}

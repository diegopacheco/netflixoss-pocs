package com.diegopacheco.github.sandbox.java.netflix.ribbon.template.annotations;

import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

import com.netflix.ribbon.Ribbon;
import com.netflix.ribbon.RibbonResponse;

public class RibbonTemplateAnnotationsRunner {
	
	static{
		System.setProperty("ribbon.listOfServers","127.0.0.1:7001");
	}
	
	public static void main(String[] args) throws Exception{
		
		ApiTemplate apiTemplate = Ribbon.from(ApiTemplate.class);
		System.out.println("Remplate: " + apiTemplate);
		
		RibbonResponse<ByteBuf> result = apiTemplate.api().withMetadata().execute();
		System.out.println("Result: " + result);
		
		ByteBuf buf = result.content();
		String json = buf.toString(Charset.forName("UTF-8" ));
		System.out.println("Result in Json: " + json);
	}
}

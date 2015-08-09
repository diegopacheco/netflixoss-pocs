package com.diegopacheco.github.sandbox.java.netflix.ribbon.template.annotations;

import io.netty.buffer.ByteBuf;

import com.netflix.ribbon.RibbonRequest;
import com.netflix.ribbon.proxy.annotation.Http;
import com.netflix.ribbon.proxy.annotation.Http.Header;
import com.netflix.ribbon.proxy.annotation.Http.HttpMethod;
import com.netflix.ribbon.proxy.annotation.TemplateName;

public interface ApiTemplate {
	
	@TemplateName("sample-client.properties")
 	@Http(
	    method = HttpMethod.GET,
	    uri = "/api",
	    headers = {@Header(name="MY_HEADER",value="123445")}
    )
    RibbonRequest<ByteBuf> api();
}

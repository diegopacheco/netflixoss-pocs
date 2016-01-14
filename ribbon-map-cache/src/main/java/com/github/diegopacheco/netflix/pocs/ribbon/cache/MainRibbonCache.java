package com.github.diegopacheco.netflix.pocs.ribbon.cache;

import java.nio.charset.Charset;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.appinfo.MyDataCenterInstanceConfig;
import com.netflix.config.ConfigurationManager;
import com.netflix.discovery.DefaultEurekaClientConfig;
import com.netflix.discovery.DiscoveryManager;
import com.netflix.ribbon.Ribbon;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

public class MainRibbonCache {
	
	@SuppressWarnings("static-access")
	public static void main(String[] args) throws Exception {
		ConfigurationManager.loadCascadedPropertiesFromResources("RibbonServiceProxy");
	    DiscoveryManager.getInstance().initComponent(new MyDataCenterInstanceConfig(), new DefaultEurekaClientConfig());
	    RibbonServiceProxy proxy =  Ribbon.from(RibbonServiceProxy.class);
	    
	    ObjectMapper mapper = new ObjectMapper();
	    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	    
	    ByteBuf bb = proxy.getWeather("London,UK").withMetadata().execute().content();
	  	String json = bb.toString(Charset.forName("UTF-8"));
	  	System.out.println(json);
	  	
	  	HashMapCacheProvider.getInstance().set("London,UK", bb);
	  	makeRibbonCall(proxy);
	  	
	  	ByteBuf buffer = Unpooled.copiedBuffer("{cache-fake:true}", CharsetUtil.UTF_8);
	  	HashMapCacheProvider.getInstance().set("London,UK", buffer);
	  	makeRibbonCall(proxy);
	  	makeRibbonCall(proxy);
	    	  		  	
	}

	private static void makeRibbonCall(RibbonServiceProxy proxy) {
		ByteBuf bb = proxy.getWeather("London,UK").withMetadata().execute().content();
	  	String json = bb.toString(Charset.forName("UTF-8"));
	  	System.out.println(json);
	}
}

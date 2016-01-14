package com.github.diegopacheco.netflix.pocs.ribbon.cache;

import com.netflix.ribbon.RibbonRequest;
import com.netflix.ribbon.proxy.annotation.CacheProvider;
import com.netflix.ribbon.proxy.annotation.ClientProperties;
import com.netflix.ribbon.proxy.annotation.ClientProperties.Property;
import com.netflix.ribbon.proxy.annotation.Http;
import com.netflix.ribbon.proxy.annotation.Http.HttpMethod;
import com.netflix.ribbon.proxy.annotation.Hystrix;
import com.netflix.ribbon.proxy.annotation.TemplateName;
import com.netflix.ribbon.proxy.annotation.Var;

import io.netty.buffer.ByteBuf;

@ClientProperties(properties = {
	    @Property(name="ReadTimeout",              value="20000"),
	    @Property(name="ConnectTimeout",           value="10000"),
	    @Property(name="MaxAutoRetriesNextServer", value="5"),
	}, exportToArchaius = true) 
public interface RibbonServiceProxy {

	  @TemplateName("getWeather")
	  @Http(method = HttpMethod.GET,
	    uri = "http://api.openweathermap.org/data/2.5/weather?q={cityName}&appid=2de143494c0b295cca9337e1e96b00e0"
	  )
	  @Hystrix(validator = ServiceResponseValidator.class)
	  @CacheProvider(key = "{cityName}", provider = CacheFactoryProvider.class)
	  RibbonRequest<ByteBuf> getWeather(@Var("cityName") String cityName);
}

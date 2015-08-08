package com.diegopacheco.github.sandbox.java.netflix.ribbon.client;

import java.net.URI;

import com.netflix.client.ClientFactory;
import com.netflix.client.IClient;
import com.netflix.client.config.DefaultClientConfigImpl;
import com.netflix.client.http.HttpRequest;
import com.netflix.client.http.HttpResponse;
import com.netflix.config.ConfigurationManager;

@SuppressWarnings({"rawtypes","unchecked"})
public class RibbonClientFun {
	public static void main(String[] args) throws Exception{
		
		ConfigurationManager.loadPropertiesFromResources("sample-client.properties");
		DefaultClientConfigImpl config = new DefaultClientConfigImpl();
		config.loadProperties("sample-client");
		System.out.println(ConfigurationManager.getConfigInstance().getProperty("sample-client.ribbon.listOfServers"));
		
		IClient client = ClientFactory.getNamedClient("sample-client");
		HttpRequest request = HttpRequest.newBuilder().uri(new URI("http://127.0.0.1:7001/api")).build();

		for (int i = 0; i < 20; i++) {
			HttpResponse response = (HttpResponse) client.execute(request,config); 
			System.out.println("Ribbon Called: " + response.getRequestedURI() + 
							   " Status : " + response.getStatus() + 
							   " Response :" + response.getEntity(String.class));
		}
		
	}
}

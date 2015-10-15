package com.github.diegopacheco.sandbox.java.memcached.bench;

import net.rubyeye.xmemcached.GetsResponse;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.utils.AddrUtil;

@SuppressWarnings("rawtypes")
public class XMemcachedMainBench {

	public static void main(String[] args) throws Throwable {
		 XMemcachedClientBuilder builder = new XMemcachedClientBuilder(AddrUtil.getAddresses("127.0.0.1:11211"));
	     MemcachedClient client = builder.build();
	     
	     client.setPrimitiveAsString(true);
	     //client.addServer("127.0.0.1:11211 127.0.0.1:11212");
	     System.out.println("Client: " + client);
	     
	     client.add("hello", 0, "dennis");
	     client.replace("hello", 0, "dennis");
	     
		 GetsResponse response=client.gets("hello");
	     Object value = response.getValue();

	     System.out.println(value);
	     System.exit(0);
	}
}

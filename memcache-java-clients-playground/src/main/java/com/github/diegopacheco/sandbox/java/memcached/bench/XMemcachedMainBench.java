package com.github.diegopacheco.sandbox.java.memcached.bench;

import net.rubyeye.xmemcached.GetsResponse;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.utils.AddrUtil;

@SuppressWarnings("rawtypes")
public class XMemcachedMainBench {

	public static void main(String[] args) throws Throwable {
		for(int i=0;i<=2;i++){
			benchInsert(10);
			benchGet(10);
			
			benchInsert(100);
			benchGet(100);
			
			benchInsert(1000);
			benchGet(1000);
			
			benchInsert(10000);
			benchGet(10000);
			
			benchInsert(100000);
			benchGet(100000);
		}
	}
	
	private static void benchInsert(int n) throws Throwable {
		MemcachedClient cluster = createCluster();
		
		double init = System.currentTimeMillis();
		for(int i=0;i<=(n-1);i++){
			cluster.add("key"+i, 0, "value"+i);
		}
		double end = System.currentTimeMillis();
		printBench("Insert " + n + " IDS ",init,end);
	}
	
	private static void benchGet(int n) throws Throwable {
		MemcachedClient cluster = createCluster();
		
		double init = System.currentTimeMillis();
		for(int i=0;i<=(n-1);i++){
			cluster.get("key"+i, 0);
		}
		double end = System.currentTimeMillis();
		printBench("Get " + n + " IDS ",init,end);
	}
	
	@SuppressWarnings("unused")
	private static void simpleCall() throws Throwable{
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
	
	private static MemcachedClient createCluster() throws Throwable{
		 XMemcachedClientBuilder builder = new XMemcachedClientBuilder(AddrUtil.getAddresses("127.0.0.1:11211"));
	     MemcachedClient client = builder.build();
	     client.setPrimitiveAsString(true);
	     return client;
	}
	
	private static void printBench(String msg,double init, double end){
		int seconds = (int) ((end - init) / 1000) % 60 ;
		System.out.println("TIME to " + msg + ": " + (end - init) + " ms - " + seconds + " s" );
	}
	
}

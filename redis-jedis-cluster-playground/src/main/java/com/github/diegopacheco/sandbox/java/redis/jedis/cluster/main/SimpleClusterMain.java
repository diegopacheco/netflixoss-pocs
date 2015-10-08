package com.github.diegopacheco.sandbox.java.redis.jedis.cluster.main;

import java.util.HashSet;
import java.util.Set;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

public class SimpleClusterMain {

	public static void main(String[] args) {
		test1kInsertsAndGets();
	}
	
	public static void test10kInsertsAndGets(){
		JedisCluster jc = createCluster();
		
		float init = System.currentTimeMillis();
		for(int i=0; i<= 9999; i++){
			jc.set("foo"+i, "bar"+i);
			jc.get("foo"+i);
		}
		float end = System.currentTimeMillis();
		System.out.println("TIME to Insert/Get 10k IDS : " + (end -init) + " ms");
	}
	
	public static void test10kInserts(){
		JedisCluster jc = createCluster();
		
		float init = System.currentTimeMillis();
		for(int i=0; i<= 9999; i++){
			jc.set("foo2"+i, "bar"+i);
		}
		float end = System.currentTimeMillis();
		System.out.println("TIME to Inserts 10k IDS : " + (end -init) + " ms");
	}
	
	public static void test1kInserts(){
		JedisCluster jc = createCluster();	
		
		float init = System.currentTimeMillis();
		for(int i=0; i<= 999; i++){
			jc.set("foo3"+i, "bar"+i);
		}
		float end = System.currentTimeMillis();
		System.out.println("TIME to Inserts 1k IDS : " + (end -init) + " ms");
	}
	
	public static void test1kInsertsAndGets(){
		JedisCluster jc = createCluster();	
		
		float init = System.currentTimeMillis();
		for(int i=0; i<= 999; i++){
			jc.set("foo4"+i, "bar"+i);
			jc.get("foo4"+i);
		}
		float end = System.currentTimeMillis();
		System.out.println("TIME to Inserts/Gets 1k IDS : " + (end -init) + " ms");
	}
	
	public static void simpleTest(){
		JedisCluster jc = createCluster();
		jc.set("foo", "bar");
		
		String value = jc.get("foo");
		System.out.println(value);
	}
	
	private static JedisCluster createCluster(){
		Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
		jedisClusterNodes.add(new HostAndPort("127.0.0.1", 30001));
		jedisClusterNodes.add(new HostAndPort("127.0.0.1", 30002));
		jedisClusterNodes.add(new HostAndPort("127.0.0.1", 30003));
		jedisClusterNodes.add(new HostAndPort("192.169.1.115", 30001));
		jedisClusterNodes.add(new HostAndPort("192.169.1.115", 30002));
		jedisClusterNodes.add(new HostAndPort("192.169.1.115", 30003));
		JedisCluster jc = new JedisCluster(jedisClusterNodes);
		return jc;
	}
	
	
}

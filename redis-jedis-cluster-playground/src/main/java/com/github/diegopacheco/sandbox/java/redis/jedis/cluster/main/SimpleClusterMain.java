package com.github.diegopacheco.sandbox.java.redis.jedis.cluster.main;

import java.util.HashSet;
import java.util.Set;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

@SuppressWarnings("resource")
public class SimpleClusterMain {

	public static void main(String[] args) {
		test10kInsertsAndGets();
	}
	
	public static void test10kInsertsAndGets(){
		Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
		jedisClusterNodes.add(new HostAndPort("127.0.0.1", 30001));
		jedisClusterNodes.add(new HostAndPort("127.0.0.1", 30002));
		jedisClusterNodes.add(new HostAndPort("127.0.0.1", 30003));
		jedisClusterNodes.add(new HostAndPort("192.169.1.115", 30001));
		jedisClusterNodes.add(new HostAndPort("192.169.1.115", 30002));
		jedisClusterNodes.add(new HostAndPort("192.169.1.115", 30003));
		JedisCluster jc = new JedisCluster(jedisClusterNodes);
		
		for(int i=0; i<= 9999; i++){
			jc.set("foo"+i, "bar"+i);
			String value = jc.get("foo"+i);
			System.out.println(value);
		}
		
	}
	
	public static void simpleTest(){
		Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
		jedisClusterNodes.add(new HostAndPort("127.0.0.1", 30001));
		jedisClusterNodes.add(new HostAndPort("127.0.0.1", 30002));
		jedisClusterNodes.add(new HostAndPort("127.0.0.1", 30003));

		JedisCluster jc = new JedisCluster(jedisClusterNodes);
		jc.set("foo", "bar");
		
		String value = jc.get("foo");
		System.out.println(value);
	}
	
	
}

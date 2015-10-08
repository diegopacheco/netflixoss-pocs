package com.github.diegopacheco.sandbox.java.redis.jedis.cluster.main;

import java.util.HashSet;
import java.util.Set;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

public class SimpleClusterMain {

	public static void main(String[] args) {
		for(int i=0;i<=2;i++){
			testInsertN(100);
			testGetN(100);
			
			testInsertN(1000);
			testGetN(1000);
			
			testInsertN(10000);
			testGetN(10000);
			
			testInsertN(100000);
			testGetN(100000);
		}
	}
	
	public static void testGetN(int numIds){
		JedisCluster jc = createCluster();	
		
		double init = System.currentTimeMillis();
		for(int i=0; i<= numIds-1; i++){
			jc.get("redisKeysN"+i);
		}
		double end = System.currentTimeMillis();
		printBench("Gets " + numIds + " IDS",init,end);
	}
	
	public static void testInsertN(int numIds){
		JedisCluster jc = createCluster();	
		
		double init = System.currentTimeMillis();
		for(int i=0; i<= numIds-1; i++){
			jc.set("redisKeysN"+i,"value"+i);
		}
		double end = System.currentTimeMillis();
		printBench("Sets " + numIds + " IDS",init,end);
	}
	
	private static void printBench(String msg,double init, double end){
		int seconds = (int) ((end - init) / 1000) % 60 ;
		System.out.println("TIME to " + msg + ": " + (end - init) + " ms - " + seconds + " s" );
	}
	
	private static JedisCluster createCluster(){
		double init = System.currentTimeMillis();
		Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
		jedisClusterNodes.add(new HostAndPort("127.0.0.1", 30001));
		jedisClusterNodes.add(new HostAndPort("127.0.0.1", 30002));
		jedisClusterNodes.add(new HostAndPort("127.0.0.1", 30003));
		jedisClusterNodes.add(new HostAndPort("192.169.1.115", 30001));
		jedisClusterNodes.add(new HostAndPort("192.169.1.115", 30002));
		jedisClusterNodes.add(new HostAndPort("192.169.1.115", 30003));
		JedisCluster jc = new JedisCluster(jedisClusterNodes);
		double end = System.currentTimeMillis();
		System.out.println("Cluster Created(California|Oregon) in : " + (end - init) + " ms");
		return jc;
	}
}

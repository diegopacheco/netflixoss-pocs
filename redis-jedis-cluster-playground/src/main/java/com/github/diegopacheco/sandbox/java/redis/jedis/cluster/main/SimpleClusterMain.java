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
		
		double init = System.currentTimeMillis();
		for(int i=0; i<= 9999; i++){
			jc.set("foo"+i, "bar"+i);
			jc.get("foo"+i);
		}
		double end = System.currentTimeMillis();
		printBench("Insert/Get 10k IDS",init,end);
	}
	
	public static void test10kInserts(){
		JedisCluster jc = createCluster();
		
		double init = System.currentTimeMillis();
		for(int i=0; i<= 9999; i++){
			jc.set("foo2"+i, "bar"+i);
		}
		double end = System.currentTimeMillis();
		printBench("Inserts 10k IDS",init,end);
	}
	
	public static void test1kInserts(){
		JedisCluster jc = createCluster();	
		
		double init = System.currentTimeMillis();
		for(int i=0; i<= 999; i++){
			jc.set("foo3"+i, "bar"+i);
		}
		double end = System.currentTimeMillis();
		printBench("Inserts 1k IDS",init,end);
	}
	
	public static void test1kInsertsAndGets(){
		JedisCluster jc = createCluster();	
		
		double init = System.currentTimeMillis();
		for(int i=0; i<= 999; i++){
			jc.set("foo4"+i, "bar"+i);
			jc.get("foo4"+i);
		}
		double end = System.currentTimeMillis();
		printBench("Inserts/Gets 1k IDS",init,end);
	}
	
	public static void simpleTest(){
		JedisCluster jc = createCluster();
		jc.set("foo", "bar");
		
		String value = jc.get("foo");
		System.out.println(value);
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

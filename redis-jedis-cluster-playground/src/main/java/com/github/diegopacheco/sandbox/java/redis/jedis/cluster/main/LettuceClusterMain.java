package com.github.diegopacheco.sandbox.java.redis.jedis.cluster.main;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisFuture;
import com.lambdaworks.redis.api.StatefulRedisConnection;

public class LettuceClusterMain {
	public static void main(String[] args) {
		for(int i=0;i<=2;i++){
			testInsertN(100);
			testGetN(100);
			
//			testInsertN(1000);
//			testGetN(1000);
//			
//			testInsertN(10000);
//			testGetN(10000);
//			
//			testInsertN(100000);
//			testGetN(100000);
		}
	}
	
	public void simpleTest(){
		  RedisClient client = RedisClient.create("redis://127.0.0.1:30006");
		  StatefulRedisConnection<String, String> connection = client.connect();
		  RedisFuture<String> value = connection.async().get("key");
		  System.out.println(value);
	}
	
	public static void testGetN(int numIds){
		RedisClient cluster = createCluster();	
		StatefulRedisConnection<String, String> connection = cluster.connect();
		
		double init = System.currentTimeMillis();
		for(int i=0; i<= numIds-1; i++){
			connection.async().get("redisKeysN"+i);
		}
		double end = System.currentTimeMillis();
		printBench("Gets " + numIds + " IDS",init,end);
	}
	
	public static void testInsertN(int numIds){
		RedisClient cluster = createCluster();	
		StatefulRedisConnection<String, String> connection = cluster.connect();
		
		double init = System.currentTimeMillis();
		for(int i=0; i<= numIds-1; i++){
			connection.async().set("redisKeysN"+i,"value"+i);
		}
		double end = System.currentTimeMillis();
		printBench("Gets " + numIds + " IDS",init,end);
	}
	
	private static void printBench(String msg,double init, double end){
		int seconds = (int) ((end - init) / 1000) % 60 ;
		System.out.println("TIME to " + msg + ": " + (end - init) + " ms - " + seconds + " s" );
	}
	
	private static RedisClient createCluster(){
		double init = System.currentTimeMillis();
		RedisClient client = RedisClient.create("redis://127.0.0.1:30006");
		double end = System.currentTimeMillis();
		System.out.println("Cluster Created(California|Oregon) in : " + (end - init) + " ms");
		return client;
	}
	
}

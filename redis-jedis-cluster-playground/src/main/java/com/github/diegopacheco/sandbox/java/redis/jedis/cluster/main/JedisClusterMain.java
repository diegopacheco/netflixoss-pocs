package com.github.diegopacheco.sandbox.java.redis.jedis.cluster.main;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

public class JedisClusterMain {

	public static void main(String[] args) {
		for(int i=0;i<=2;i++){
			testInsertNTthreads(100);
			testGetNTthreads(100);
			
			testInsertNTthreads(1000);
			testGetNTthreads(1000);
			
			testInsertNTthreads(10000);
			testGetNTthreads(10000);
			
			testInsertNTthreads(100000);
			testGetNTthreads(100000);
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
	
	public static void testGetNTthreads(int numIds){
		ExecutorService execService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
		JedisCluster jc = createCluster();	
		
		double init = System.currentTimeMillis();
		for(int i=0; i<= numIds-1; i++){
			execService.execute(new JedisCallback(jc,i,true));
		}
		double end = System.currentTimeMillis();
		printBench("Gets " + numIds + " IDS",init,end);
	}
	
	public static void testInsertNTthreads(int numIds){
		ExecutorService execService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
		JedisCluster jc = createCluster();	
		
		double init = System.currentTimeMillis();
		for(int i=0; i<= numIds-1; i++){
			execService.execute(new JedisCallback(jc,i,false));
		}
		double end = System.currentTimeMillis();
		printBench("Gets " + numIds + " IDS",init,end);
	}
	
	public static void testGetNTthreadsForkJoin(int numIds){
		ExecutorService execService = new ForkJoinPool(Runtime.getRuntime().availableProcessors() * 2);
		JedisCluster jc = createCluster();	
		
		double init = System.currentTimeMillis();
		for(int i=0; i<= numIds-1; i++){
			execService.execute(new JedisCallback(jc,i,true));
		}
		double end = System.currentTimeMillis();
		printBench("Gets " + numIds + " IDS",init,end);
	}
	
	public static void testInsertNTthreadsForkJoin(int numIds){
		ExecutorService execService = new ForkJoinPool(Runtime.getRuntime().availableProcessors() * 2);
		JedisCluster jc = createCluster();	
		
		double init = System.currentTimeMillis();
		for(int i=0; i<= numIds-1; i++){
			execService.execute(new JedisCallback(jc,i,false));
		}
		double end = System.currentTimeMillis();
		printBench("Gets " + numIds + " IDS",init,end);
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

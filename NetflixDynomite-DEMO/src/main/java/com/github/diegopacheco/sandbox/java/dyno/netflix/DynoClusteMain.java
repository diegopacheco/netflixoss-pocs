package com.github.diegopacheco.sandbox.java.dyno.netflix;

import com.netflix.dyno.jedis.DynoJedisClient;

public class DynoClusteMain {
	
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
		
		System.exit(0);
	}
	
	private static void printBench(String msg,double init, double end){
		int seconds = (int) ((end - init) / 1000) % 60 ;
		System.out.println("TIME to " + msg + ": " + (end - init) + " ms - " + seconds + " s" );
	}

	private static void benchInsert(int n) throws Throwable {
		DynoJedisClient cluster = DynomiteClusterManager.connect();
		
		double init = System.currentTimeMillis();
		for(int i=0;i<=(n-1);i++){
			cluster.set("key"+i, "value"+i);
		}
		double end = System.currentTimeMillis();
		printBench("Insert " + n + " IDS ",init,end);
	}
	
	private static void benchGet(int n) throws Throwable {
		DynoJedisClient cluster = DynomiteClusterManager.connect();
		
		double init = System.currentTimeMillis();
		for(int i=0;i<=(n-1);i++){
			cluster.get("key"+i);
		}
		double end = System.currentTimeMillis();
		printBench("Get " + n + " IDS ",init,end);
	}
	
}

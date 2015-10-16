package com.github.diegopacheco.sandbox.java.memcached.bench.evcache.netflix;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.evcache.EVCache;
import com.netflix.evcache.pool.EVCacheClientPoolManager;

public class NetflixEVcacheMainBench {
	
	private static Logger log = LoggerFactory.getLogger(NetflixEVcacheMainBench.class);
	
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
	
	private static void benchInsert(int n) throws Throwable {
		EVCache cluster = createCluster();
		
		double init = System.currentTimeMillis();
		for(int i=0;i<=(n-1);i++){
			cluster.set("key"+i, "value"+i,90000);
		}
		double end = System.currentTimeMillis();
		printBench("Insert " + n + " IDS ",init,end);
	}
	
	private static void benchGet(int n) throws Throwable {
		EVCache cluster = createCluster();
		
		double init = System.currentTimeMillis();
		for(int i=0;i<=(n-1);i++){
			cluster.get("key"+i);
		}
		double end = System.currentTimeMillis();
		printBench("Get " + n + " IDS ",init,end);
	}
	
	@SuppressWarnings("unused")
	private void simpleCall() throws Throwable {
		System.setProperty("net.spy.log.LoggerImpl", "net.spy.memcached.compat.log.Log4JLogger");
        System.setProperty("log4j.rootLogger", "ERROR");
        BasicConfigurator.configure();
        log.info("Logger intialized");
		
		System.setProperty("evcache.pool.provider", "com.netflix.evcache.pool.standalone.SimpleEVCacheClientPoolImpl");
		System.setProperty("EVCACHE_CUSTOMER.EVCacheClientPool.hosts", "127.0.0.1:11211");
		EVCacheClientPoolManager.getInstance().initEVCache("EVCACHE_CUSTOMER");
		Thread.sleep(2000);
		
		EVCache evCache = (new EVCache.Builder()).setAppName("EVCACHE_CUSTOMER").setCacheName("cid").enableZoneFallback().build();
		System.out.println("EVCache: " + evCache);
		
		evCache.set("Hello", "World", 90000);
		String value = evCache.<String>get("Hello");
		System.out.println("Value: " + value);
	}
	
	private static void printBench(String msg,double init, double end){
		int seconds = (int) ((end - init) / 1000) % 60 ;
		System.out.println("TIME to " + msg + ": " + (end - init) + " ms - " + seconds + " s" );
	}

	private static EVCache createCluster() throws Throwable {
		System.setProperty("net.spy.log.LoggerImpl", "net.spy.memcached.compat.log.Log4JLogger");
        System.setProperty("log4j.rootLogger", "ERROR");
        BasicConfigurator.configure();
        log.info("Logger intialized");
		
		System.setProperty("evcache.pool.provider", "com.netflix.evcache.pool.standalone.SimpleEVCacheClientPoolImpl");
		System.setProperty("EVCACHE_CUSTOMER.EVCacheClientPool.hosts", "127.0.0.1:11211");
		EVCacheClientPoolManager.getInstance().initEVCache("EVCACHE_CUSTOMER");
		Thread.sleep(2000);
		
		EVCache evCache = (new EVCache.Builder()).setAppName("EVCACHE_CUSTOMER").setCacheName("cid").enableZoneFallback().build();
		return evCache;
	}
	
}

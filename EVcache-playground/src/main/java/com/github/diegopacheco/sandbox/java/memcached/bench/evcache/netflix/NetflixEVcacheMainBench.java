package com.github.diegopacheco.sandbox.java.memcached.bench.evcache.netflix;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.evcache.EVCache;
import com.netflix.evcache.pool.EVCacheClientPoolManager;


public class NetflixEVcacheMainBench {
	
	private static Logger log = LoggerFactory.getLogger(NetflixEVcacheMainBench.class);
	
	public static void main(String[] args) throws Throwable {
		
		System.setProperty("net.spy.log.LoggerImpl", "net.spy.memcached.compat.log.Log4JLogger");
        System.setProperty("log4j.rootLogger", "ERROR");
        BasicConfigurator.configure();
        log.info("Logger intialized");
		
		System.setProperty("evcache.pool.provider", "com.netflix.evcache.pool.standalone.SimpleEVCacheClientPoolImpl");
		System.setProperty("EVCACHE_CUSTOMER.EVCacheClientPool.hosts", "127.0.0.1:11211>");
		EVCacheClientPoolManager.getInstance().initEVCache("EVCACHE_CUSTOMER");
		Thread.sleep(2000);
		
		EVCache evCache = (new EVCache.Builder()).setAppName("EVCACHE_CUSTOMER").setCacheName("cid").enableZoneFallback().build();
		System.out.println("EVCache: " + evCache);
		
		evCache.set("Hello", "World", 90000);
		String value = evCache.get("Hello");
		System.out.println("Value: " + value);
		
		System.exit(0);
		
	}
}

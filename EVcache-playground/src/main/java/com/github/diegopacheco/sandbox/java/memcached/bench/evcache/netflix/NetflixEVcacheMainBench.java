package com.github.diegopacheco.sandbox.java.memcached.bench.evcache.netflix;

import com.netflix.evcache.EVCache;
import com.netflix.evcache.pool.EVCacheClientPoolManager;


public class NetflixEVcacheMainBench {
	public static void main(String[] args) throws Throwable {
		
		System.setProperty("evcache.pool.provider", "com.netflix.evcache.pool.standalone.SimpleEVCacheClientPoolImpl");
		System.setProperty("EVCACHE_CUSTOMER.EVCacheClientPool.hosts", "127.0.0.1:11211>");
		
		EVCacheClientPoolManager.getInstance().initEVCache("EVCACHE_CUSTOMER");
		
		EVCache evCache = (new EVCache.Builder()).setAppName("EVCACHE_CUSTOMER").setCacheName("cid").enableZoneFallback().build();
		System.out.println("EVCache: " + evCache);
		
		evCache.set("Hello", "World", 90000);
		String value = evCache.get("Hello");
		System.out.println("Value: " + value);
		
		System.exit(0);
		
	}
}

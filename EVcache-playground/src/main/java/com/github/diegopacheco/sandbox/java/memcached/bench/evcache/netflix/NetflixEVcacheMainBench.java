package com.github.diegopacheco.sandbox.java.memcached.bench.evcache.netflix;

import net.spy.memcached.CachedData;

import com.netflix.evcache.EVCacheTranscoder;
import com.netflix.evcache.pool.EVCacheClient;
import com.netflix.evcache.pool.EVCacheClientPool;
import com.netflix.evcache.pool.EVCacheClientPoolManager;

public class NetflixEVcacheMainBench {
	public static void main(String[] args) throws Throwable {
		EVCacheClientPoolManager.getInstance().initEVCache("SimpleEvCacheBench");
		EVCacheClientPoolManager pm = EVCacheClientPoolManager.getInstance();
		
		EVCacheClientPool pool = pm.getEVCacheClientPool("SimpleEvCacheBench");
		EVCacheClient client = pool.getEVCacheClient();
		
		client.set("key1",new EVCacheTranscoder<String>() {
			@Override
			public boolean asyncDecode(CachedData arg0) {
				return false;
			}
			@Override
			public String decode(CachedData arg0) {
				return null;
			}
			@Override
			public CachedData encode(String arg0) {
				return null;
			}
			@Override
			public int getMaxSize() {
				return 100;
			}
		},"",0);
		
		String result = client.get("key1", new EVCacheTranscoder<String>() {
			@Override
			public boolean asyncDecode(CachedData arg0) {
				return false;
			}
			@Override
			public String decode(CachedData arg0) {
				return null;
			}
			@Override
			public CachedData encode(String arg0) {
				return null;
			}
			@Override
			public int getMaxSize() {
				return 100;
			}
		});
		
		System.out.println(result);
		
	}
}

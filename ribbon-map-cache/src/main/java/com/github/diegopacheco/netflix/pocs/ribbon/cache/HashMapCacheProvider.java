package com.github.diegopacheco.netflix.pocs.ribbon.cache;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.netflix.ribbon.CacheProvider;

import io.netty.buffer.ByteBuf;
import rx.Observable;

public class HashMapCacheProvider implements CacheProvider<ByteBuf> {
	
	private static HashMapCacheProvider instance = null;
	private HashMapCacheProvider() {}
	
	public static synchronized HashMapCacheProvider getInstance(){
		if (instance==null)
			instance = new HashMapCacheProvider();
		return instance;
	}
	
	private static Map<String,ByteBuf> cache = new HashMap<>();
	
	@Override
	public Observable<ByteBuf> get(String keyTemplate, Map<String, Object> requestProperties) {
		if (cache.get(keyTemplate)!=null)
			return Observable.from( Arrays.asList( cache.get(keyTemplate) ) );
		return null;
	}
	
	public static void set(String key,ByteBuf value){
		cache.put(key, value);
	}
	
	public static ByteBuf getAsByteBuf(String key){
		return cache.get(key);
	}
	
}

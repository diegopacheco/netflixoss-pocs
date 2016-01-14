package com.github.diegopacheco.netflix.pocs.ribbon.cache;

import com.netflix.ribbon.CacheProvider;
import com.netflix.ribbon.CacheProviderFactory;

import io.netty.buffer.ByteBuf;

public class CacheFactoryProvider implements CacheProviderFactory<ByteBuf> {
	  private static HashMapCacheProvider provider = HashMapCacheProvider.getInstance();

	  public CacheProvider<ByteBuf> createCacheProvider() {
		  return provider;
	  }
}
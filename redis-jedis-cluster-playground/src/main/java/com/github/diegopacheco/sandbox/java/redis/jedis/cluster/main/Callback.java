package com.github.diegopacheco.sandbox.java.redis.jedis.cluster.main;

import redis.clients.jedis.JedisCluster;

public class Callback implements Runnable{
	
	private JedisCluster jc;
	private int i;
	private Boolean get = false;
	
	public Callback(JedisCluster jc,int i,Boolean get) {
		this.jc = jc;
		this.i = i;
		this.get = get;
	}
	
	@Override
	public void run() {
		if(get){
			jc.get("redisKeysN"+i);
		} else {
			jc.set("redisKeysN"+i,"value"+i);
		}
	}
}

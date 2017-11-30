package com.github.diegopacheco.netflixoss.pocs.dyno.queues.fun;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.github.diegopacheco.netflixoss.pocs.dyno.DynoConnectionManager;
import com.netflix.dyno.jedis.DynoJedisClient;
import com.netflix.dyno.queues.DynoQueue;
import com.netflix.dyno.queues.Message;
import com.netflix.dyno.queues.redis.DynoShardSupplier;
import com.netflix.dyno.queues.redis.RedisQueues;

public class DynoQueuesMain {
	
	public static void main(String[] args) {
		
		String region = "dc";
		String localDC = "rack1";
		
		String prefix = "dynoQueue_";
		int dynoThreadCount = 20;
		
		DynoJedisClient dyno = DynoConnectionManager.build();
		
		DynoShardSupplier ss = new DynoShardSupplier(dyno.getConnPool().getConfiguration().getHostSupplier(), region, localDC);
		
		RedisQueues queues = new RedisQueues(dyno, dyno, prefix, ss, 60_000, 60_000, dynoThreadCount);
		
		String queueName = "msg_queue";
		DynoQueue queue = queues.get(queueName);
		
		Message msg = new Message("id1", "message payload");
		queue.push(Arrays.asList(msg));
		
		int count = 10;
		List<Message> polled = queue.pop(count, 1, TimeUnit.SECONDS);	
		System.out.println(polled);
		
		queue.ack("id1");
	}
	
}

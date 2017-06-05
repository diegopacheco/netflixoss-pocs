package com.github.diegopacheco.netflixpocs.redis.lettuce;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.api.rx.RedisStringReactiveCommands;

import rx.Observable;
import rx.functions.Action1;

//
// https://github.com/lettuce-io/lettuce-core/wiki/Reactive-API-(4.0)
//
public class SimpleLettuceReactiveMain {
	
	public static void main(String[] args) throws Throwable {

		RedisClient client = RedisClient.create("redis://localhost");
		RedisStringReactiveCommands<String, String> commands = client.connect().reactive();

		commands.get("k1").subscribe(new Action1<String>() {
			@Override
			public void call(String value) {
				System.out.println(value);
			}
		});
		
		Observable
		   .just("k1", "k2", "k3")
		   .flatMap(commands::get)
		   .subscribe(document -> System.out.println("Got value: " + document));
		
		Thread.sleep(2000L);

	}
}

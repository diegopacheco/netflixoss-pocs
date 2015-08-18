package com.github.diegopacheco.sandbox.java.guava.fun;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import net.jodah.expiringmap.ExpiringMap;

public class ExpiringMapMain {
	public static void main(String[] args) throws Throwable {
		
		final Map<Integer, Object> map = ExpiringMap.builder()
		  .expiration(10, TimeUnit.SECONDS)
		  .build();
		
		map.put("1".hashCode(), "1");
		map.put("2".hashCode(), "2");
		map.put("3".hashCode(), "3");
		
		System.out.println(map.get("1".hashCode()));
		System.out.println(map.get("2".hashCode()));
		System.out.println(map.get("3".hashCode()));
		
		(new Thread(new Runnable(){
			   public void run(){
					System.out.println(map.get("1".hashCode()));
					System.out.println(map.get("2".hashCode()));
					System.out.println(map.get("3".hashCode()));
			   }
			})).start();
		
		(new Thread(new Runnable(){
			   public void run(){
					System.out.println(map.get("1".hashCode()));
					System.out.println(map.get("2".hashCode()));
					System.out.println(map.get("3".hashCode()));
			   }
			})).start();		
		
		Thread.sleep(10000L);
		
		System.out.println(map.get("1".hashCode()));
		System.out.println(map.get("2".hashCode()));
		System.out.println(map.get("3".hashCode()));
		
		(new Thread(new Runnable(){
			   public void run(){
					System.out.println(map.get("1".hashCode()));
					System.out.println(map.get("2".hashCode()));
					System.out.println(map.get("3".hashCode()));
			   }
			})).start();				
		
		System.exit(0);
	}
}

package com.github.diegopacheco.sandbox.java.commoms.collections4.fun;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.map.PassiveExpiringMap;
import org.apache.commons.collections4.map.PassiveExpiringMap.ConstantTimeToLiveExpirationPolicy;
import org.apache.commons.collections4.map.PassiveExpiringMap.ExpirationPolicy;

public class PassiveExpiringMapMain {
public static void main(String[] args) throws Throwable {
		
		final ExpirationPolicy<Integer, Object> policy = new ConstantTimeToLiveExpirationPolicy<>(10, TimeUnit.SECONDS);
		final Map<Integer, Object> map = new PassiveExpiringMap<>(policy);
		
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

package com.github.diegopacheco.sandbox.java.netflixoss.rxjava;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action1;

public class IntervalMain {
	public static void main(String... args) throws Throwable{
	    Observable
	        .interval(1, TimeUnit.SECONDS)
	        .subscribe(new Action1<Long>() {
	            @Override
	            public void call(Long counter) {
	                System.out.println("Got: " + counter);
	            }
	    });
	    
	    while(true){
	    	Thread.sleep(1000L);
	    }
	}
}

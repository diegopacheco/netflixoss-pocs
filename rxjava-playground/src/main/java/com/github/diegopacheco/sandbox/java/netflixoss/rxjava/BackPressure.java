package com.github.diegopacheco.sandbox.java.netflixoss.rxjava;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.schedulers.Schedulers;

public class BackPressure {
	public static void main(String[] args) throws InterruptedException {
		
		Observable.interval(1, TimeUnit.MILLISECONDS)
	    .onBackpressureBuffer(1000)
	    .observeOn(Schedulers.newThread())
	    .subscribe(
	        i -> {
	            System.out.println(i);
	            try {
	                Thread.sleep(100);
	            } catch (Exception e) { }
	        },
	        System.out::println
	    );
		
		while(true) Thread.sleep(1000);
		
	}
}

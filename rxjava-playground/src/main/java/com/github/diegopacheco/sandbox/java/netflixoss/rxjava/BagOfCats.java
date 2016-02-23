package com.github.diegopacheco.sandbox.java.netflixoss.rxjava;

import java.util.concurrent.TimeUnit;

import rx.Observable;

public class BagOfCats {
	public static void main(String[] args) {
		
		Observable<Integer> values = Observable.range(0,4);
		values
		    .map(i -> i + 3)
		    .subscribe(new PrintSubscriber("Map"));
		
		System.out.println("---");
		values = 
		        Observable.just("0", "1", "2", "3")
		            .map(Integer::parseInt);
		values.subscribe(new PrintSubscriber("Map"));
		
		System.out.println("---");
		Observable<Long> values2 = Observable.interval(1, TimeUnit.MILLISECONDS);
		values2.take(3)
		    .timeInterval()
		    .subscribe(new PrintSubscriber("TimeInterval"));
		
		System.out.println("---");
	    Observable.range(1, 100)
        .groupBy(n -> n % 2 == 0)
        .flatMap(g -> {
            return g.filter(i -> i <= 20).toList();
        }).forEach(System.out::println);
	    
	    System.out.println("---");
        Observable.create(s -> {
            throw new RuntimeException("failed");
        }).subscribe(System.out::println, t -> System.out.println("2) Error: " + t));
	    
	}
}

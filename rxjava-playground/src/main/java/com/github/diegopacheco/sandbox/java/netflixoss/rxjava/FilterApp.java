package com.github.diegopacheco.sandbox.java.netflixoss.rxjava;

import rx.Observable;
import rx.Subscription;

public class FilterApp {
	public static void main(String[] args) {
		
		Observable<Integer> values = Observable.range(0,10);
		Subscription oddNumbers = values
		    .filter(v -> v % 2 == 0)
		    .subscribe(
		        v -> System.out.println(v),
		        e -> System.out.println("Error: " + e),
		        () -> System.out.println("Completed")
		    );
		
	}
}

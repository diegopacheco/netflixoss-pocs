package com.github.diegopacheco.sandbox.java.netflixoss.rxjava;

import rx.Observable;
import rx.Subscription;

public class TakeApp {
	public static void main(String[] args) {
		Observable<Integer> values = Observable.range(0, 5);

		Subscription first2 = values
		    .take(2)
		    .subscribe(
		        v -> System.out.println(v),
		        e -> System.out.println("Error: " + e),
		        () -> System.out.println("Completed")
		    );
		System.out.println(first2);
	}
}

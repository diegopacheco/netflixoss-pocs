package com.github.diegopacheco.sandbox.java.netflixoss.rxjava;

import rx.Observable;

public class WindowApp {
	public static void main(String[] args) {
		
		Observable.range(0, 5)
	    .window(3, 1)
	    .flatMap(o -> o.toList())
	    .subscribe(System.out::println);
		
	}
}

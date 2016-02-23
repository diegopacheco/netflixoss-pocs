package com.github.diegopacheco.sandbox.java.netflixoss.rxjava;

import rx.Observable;

public class ObMainMaps {
	public static void main(String[] args) {
		Observable.just("Hello","World","SP")
	    .map(s -> s + " -Diego RxJava")
	    .map(s -> s.hashCode())
	    .map(i -> Integer.toString(i))
	    .subscribe(s -> System.out.println(s));
	}
}

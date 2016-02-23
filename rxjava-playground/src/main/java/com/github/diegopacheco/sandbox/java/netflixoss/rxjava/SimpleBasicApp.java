package com.github.diegopacheco.sandbox.java.netflixoss.rxjava;

import rx.Observable;
import rx.functions.Action1;

public class SimpleBasicApp {
	
	
	public static void hello(String... names) {
	    Observable.from(names).subscribe(new Action1<String>() {
	        @Override
	        public void call(String s) {
	            System.out.println("Hello " + s + "!");
	        }
	    });
	}
	
	public static void main(String[] args) {
		hello("Ben", "George");
		hello("Diego", "Pacheco");
		hello("Rx", "Java");
	}
	
	
}

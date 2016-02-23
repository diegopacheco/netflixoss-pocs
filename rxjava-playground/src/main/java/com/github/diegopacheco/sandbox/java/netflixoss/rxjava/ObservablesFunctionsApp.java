package com.github.diegopacheco.sandbox.java.netflixoss.rxjava;

import rx.Observable;
import rx.functions.Func1;

public class ObservablesFunctionsApp {
	public static void main(String[] args) {
		
		Observable.just("Hello, world!","QCon","SP")
	    .map(new Func1<String, String>() {
	        @Override
	        public String call(String s) {
	            return s + " -Diego RxJava";
	        }
	    })
	    .subscribe(s -> System.out.println(s));
		
	}
}

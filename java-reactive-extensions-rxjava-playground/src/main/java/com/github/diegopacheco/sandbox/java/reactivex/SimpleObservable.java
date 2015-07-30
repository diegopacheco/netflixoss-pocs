package com.github.diegopacheco.sandbox.java.reactivex;

import java.util.Arrays;

import rx.Observable;
import rx.functions.Action1;

public class SimpleObservable {
	public static void main(String[] args) {
		Observable.from(Arrays.asList(new String[]{"RX","Java","Simple"})).subscribe(new Action1<String>() {
	        @Override
	        public void call(String s) {
	            System.out.println("Hello " + s + "!");
	        }
	    });
	}
}

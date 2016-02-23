package com.github.diegopacheco.sandbox.java.netflixoss.rxjava;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public class FizzBuzzWithObservablesApp {
	public static void main(String[] args) throws InterruptedException {
		Observable
		    .interval(10, TimeUnit.MILLISECONDS)
		    .take(20)
		    .map(new Func1<Long, String>() {
		        @Override
		        public String call(Long input) {
		            if (input % 3 == 0) {
		                return "Fizz";
		            } else if (input % 5 == 0) {
		                return "Buzz";
		            }
		            return Long.toString(input);
		        }
		    })
		    .toBlocking()
		    .forEach(new Action1<String>() {
		        @Override
		        public void call(String s) {
		            System.out.println(s);
		        }
		    });
		
		   while(true){
		    	Thread.sleep(1000L);
		    }
		
	}
}

package com.github.diegopacheco.sandbox.java.netflixoss.rxjava;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import rx.Observable;

public class CreatingObservables {
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		
		Observable<String> o = Observable.from(new Future<String>(){
			@Override
			public boolean cancel(boolean mayInterruptIfRunning) {
				return false;
			}
			@Override
			public boolean isCancelled() {
				return false;
			}
			@Override
			public boolean isDone() {
				return false;
			}
			@Override
			public String get() throws InterruptedException, ExecutionException {
				return "a,b,c";
			}
			@Override
			public String get(long timeout, TimeUnit unit)
					throws InterruptedException, ExecutionException, TimeoutException {
				return null;
			}
		});
		System.out.println(o);
		o.toBlocking().forEach(System.out::println);
		
		Observable<String> o2 = Observable.just("one object");
		System.out.println(o2);
		o2.toBlocking().forEach(System.out::println);
	}
}

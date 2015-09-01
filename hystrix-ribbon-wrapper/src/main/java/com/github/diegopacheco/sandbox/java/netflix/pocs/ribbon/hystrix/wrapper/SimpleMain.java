package com.github.diegopacheco.sandbox.java.netflix.pocs.ribbon.hystrix.wrapper;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixObservableCommand;

import rx.Observable;
import rx.Subscriber;

public class SimpleMain {
	public static void main(String[] args) {
		String s = new CommandHelloWorld("World").observe().toBlocking().first();
		System.out.println(s);
	}
	
	static class CommandHelloWorld extends HystrixObservableCommand<String> {

	    private final String name;

	    public CommandHelloWorld(String name) {
	        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
	        this.name = name;
	    }

	    @Override
	    protected Observable<String> construct() {
	        return Observable.create(new Observable.OnSubscribe<String>() {
	            @Override
	            public void call(Subscriber<? super String> observer) {
	                try {
	                    if (!observer.isUnsubscribed()) {
	                        observer.onNext("Hello");
	                        observer.onNext(name + "!");
	                        observer.onCompleted();
	                    }
	                } catch (Exception e) {
	                    observer.onError(e);
	                }
	            }
	         } );
	    }
	}
	
}

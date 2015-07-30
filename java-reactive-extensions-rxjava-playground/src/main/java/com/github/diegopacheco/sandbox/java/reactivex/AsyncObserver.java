package com.github.diegopacheco.sandbox.java.reactivex;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;

public class AsyncObserver {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
	     Observable asyncObservable = Observable.create(new Observable.OnSubscribe() {
				@Override
				public void call(Object subscriber) {
					final Subscriber mySubscriber = (Subscriber)subscriber;

	                Thread thread = new Thread(new Runnable() {
	                    @Override
	                    public void run() {
	                        for (int ii = 0; ii < 10; ii++) {
	                            if (!((Subscription) subscriber).isUnsubscribed()) {
	                                ((Observer) subscriber).onNext("Pushing value from async thread " + ii);
	                            }
	                        }
	                    }
	                });
	                thread.start();
				}
	        });

	        asyncObservable.skip(5).subscribe(new Action1<String>() {
	            @Override
	            public void call(String incomingValue) {
	                System.out.println(incomingValue);
	            }
	        });

	}
}

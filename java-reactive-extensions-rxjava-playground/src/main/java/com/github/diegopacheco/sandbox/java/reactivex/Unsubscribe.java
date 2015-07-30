package com.github.diegopacheco.sandbox.java.reactivex;

import rx.Observable;

public class Unsubscribe {
	public static void main(String[] args) {
		Observable.create( sub -> { 
			for(int i=0; !sub.isUnsubscribed(); i++){
				sub.onNext(i);
				System.out.println("Emitted: " + i);
			}
			sub.onCompleted();
		}).take(10)
		  .subscribe( asLong -> {
			System.out.println("Got: " + asLong);  
	    }); 
	}
}

package com.github.diegopacheco.sandbox.java.reactivex;

import rx.Observable;

public class ObservableBasics {
	public static void main(String[] args) {
		Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
		  .skip(2)
		  .take(4)
		  .map( i ->  i + 1 )
		  .subscribe( i -> System.out.println("next => " + i));	
		
	}
}

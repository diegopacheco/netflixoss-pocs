package com.github.diegopacheco.sandbox.java.reactivex;

import rx.Observable;

public class Java8Fun {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		Integer[] numbers = { 0, 1, 2, 3, 4, 5 };
        @SuppressWarnings("rawtypes")
		Observable numberObservable = Observable.from(numbers);
        numberObservable.subscribe(
                (incomingNumber) -> System.out.println("incomingNumber " + incomingNumber),
                (error) -> System.out.println("Something went wrong" + ((Throwable)error).getMessage()),
                () -> System.out.println("This observable is finished")
        );
	}
}

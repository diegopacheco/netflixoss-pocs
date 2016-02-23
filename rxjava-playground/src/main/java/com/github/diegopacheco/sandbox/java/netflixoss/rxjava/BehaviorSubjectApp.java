package com.github.diegopacheco.sandbox.java.netflixoss.rxjava;

import rx.subjects.BehaviorSubject;

public class BehaviorSubjectApp {
	public static void main(String[] args) {
		
		BehaviorSubject<Integer> s = BehaviorSubject.create();
		s.subscribe(
		    v -> System.out.println("Late: " + v),
		    e -> System.out.println("Error"),
		    () -> System.out.println("Completed")
		);
		s.onNext(0);
		s.onNext(1);
		s.onNext(2);
		s.onCompleted();
		
	}
}

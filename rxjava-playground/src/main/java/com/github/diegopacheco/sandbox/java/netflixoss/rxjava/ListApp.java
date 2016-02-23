package com.github.diegopacheco.sandbox.java.netflixoss.rxjava;

import java.util.List;

import rx.Observable;

public class ListApp {
	public static void main(String[] args) {
		
		List<Integer> list = Observable
			    .just(1, 2, 3)
			    .toList()
			    .toBlocking()
			    .single();

		System.out.println(list);	
		
	}
}

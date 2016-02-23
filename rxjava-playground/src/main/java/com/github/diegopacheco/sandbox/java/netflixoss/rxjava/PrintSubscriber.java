package com.github.diegopacheco.sandbox.java.netflixoss.rxjava;

import rx.functions.Action1;

public class PrintSubscriber implements Action1<Object> {
	
	private String name;
	
	public PrintSubscriber(String name) {
		this.name = name;
	}
	
	@Override
	public void call(Object t) {
		System.out.println(name + " - hey:" + t);
	}
}

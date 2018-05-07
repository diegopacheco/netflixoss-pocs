package com.github.diegopacheco.sandbox.java.netflixoss.karyon.poc.jersey.ex;

public class DivisionByZeroException extends RuntimeException {
		
	private static final long serialVersionUID = 1L;

	public DivisionByZeroException() {}
	
	public DivisionByZeroException(String msg) {
		super(msg);
	}
	
	public DivisionByZeroException(String msg,Exception e) {
		super(msg,e);
	}
	
}

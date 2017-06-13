package com.github.diegopacheco.sandbox.java.netflixoss.karyon.rest;

import java.util.Stack;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.github.diegopacheco.sandbox.java.netflixoss.karyon.ribbon.RibbonMathClient;

@Singleton
public class CalcService {
	
	private RibbonMathClient client;
	
	@Inject
	public CalcService(RibbonMathClient client) {
		this.client = client;
	}
	
	public Double calc(String expr){
		
		if ( expr==null || ("".equals(expr)) ) 
			throw new IllegalArgumentException("You must pass a valid expression. ");
		
		Stack<String> stack = new Stack<String>();
		String s1;
		Double a;
		Double b;
		Double result = 0.0;
		
		for (int i = 0; i < expr.length(); i++){
		     s1 = expr.charAt(i) + "";
		     
		     if (" ".equals(s1)) continue;
		     
		     if(s1.equals("+") || s1.equals("-") || s1.equals("*") || s1.equals("/")){
		          switch (s1){
		                case "+":
		                    a = new Double(stack.pop());
		                    b = new Double(stack.pop());
		                    result = client.sum(a, b).toBlocking().first();
		                    break;
		                case "-":
		                	a = new Double(stack.pop());
		                    b = new Double(stack.pop());
		                    result = client.sub(a, b).toBlocking().first();
		                    break;
		                case "/":
		                	a = new Double(stack.pop());
		                    b = new Double(stack.pop());
		                    result = client.div(a, b).toBlocking().first();
		                    break;
		                case "*":
		                	a = new Double(stack.pop());
		                    b = new Double(stack.pop());
		                    result = client.mul(a, b).toBlocking().first();
		                    break;
		                }
		         }else{
		             stack.push(s1);
		         }
		}
		return result;
		
	}
	
}

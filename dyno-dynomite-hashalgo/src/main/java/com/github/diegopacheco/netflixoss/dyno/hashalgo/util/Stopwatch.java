package com.github.diegopacheco.netflixoss.dyno.hashalgo.util;

import java.util.concurrent.TimeUnit;

public class Stopwatch {
	
	private Long init  = 0L;
	private Long end   = 0L; 
	private Long total = 0L;
	
	public Stopwatch start(){
		this.init = System.currentTimeMillis();
		return this;
	}
	
	public Stopwatch stop(){
		this.end = System.currentTimeMillis();
		this.total = this.end - this.init;
		return this;
	}
	
	public Long getTotalExecution(){
		return this.total;
	}
	
	public void printExecutionTime(){
		System.out.println("Completed in: " +  String.format("[%d min, %d sec] [%d millis]", 
			    TimeUnit.MILLISECONDS.toMinutes(total),
			    TimeUnit.MILLISECONDS.toSeconds(total) - 
			    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(total)),
			    TimeUnit.MILLISECONDS.toMillis(total) 
		));
	}
	
}

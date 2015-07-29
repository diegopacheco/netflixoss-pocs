package com.github.diegopacheco.java.sanbox.cassandradatastax.main.stmt.rx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Command {
	
	private String args;
	
	private static final Logger logger   = LoggerFactory.getLogger("com.github.diegopacheco.java.sanbox.cassandradatastax.main.stmt.rx.Command");
	
	public Command(String args) {
		this.args = args;
	}
	
	public void run(){
		logger.info("Got: " + args);
		try {
			Thread.sleep(5000L);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		logger.info("Done!");
	}
	
	@Override
	public String toString() {
		return "Command[" + args + "]";
	}
	
	public static Command of(String args){
		return new Command(args);
	}
	
}

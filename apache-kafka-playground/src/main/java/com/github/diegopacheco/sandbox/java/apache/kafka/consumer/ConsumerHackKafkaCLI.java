package com.github.diegopacheco.sandbox.java.apache.kafka.consumer;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class ConsumerHackKafkaCLI {
	public static void main(String[] args) throws Throwable {
		Process p = Runtime.getRuntime().exec("d:\\diego\\bin\\kafka_2.11-0.8.2.1\\bin\\windows\\kafka-console-consumer.bat " + 
									          "--zookeeper localhost:2181 --topic page_visits --from-beginning");
		
		System.out.println(p);
		
		BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line = "";
		while ((line = input.readLine()) != null) {
		    System.out.println(line);
		}
		input.close();
		
	}
}

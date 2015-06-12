package com.github.diegopacheco.sandbox.java.cassandra.springdata;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.cassandra.core.CassandraOperations;

import com.datastax.driver.core.Cluster;

public class SpringJavaConfigApp {
	public static void main(String[] args) {
		
		ApplicationContext context = new AnnotationConfigApplicationContext( CassandraConfig.class );
		
		Cluster cluster = (Cluster)context.getBean("cluster");
		System.out.println(cluster);
		
		CassandraOperations co = (CassandraOperations) context.getBean("cassandraTemplate");
		System.out.println(co);
		
		SimpleService ss = (SimpleService) context.getBean(SimpleService.class);
		ss.insert();
		ss.select();
		
		PersonRepository pr = context.getBean(PersonRepository.class);
		System.out.println(pr);
		System.out.println(pr.findAll());
		
//		try (Stream<Person> stream = pr.findAllByPachecoStream()){
//			stream.forEach(e -> System.out.println(e));
//		}
		
	}
}

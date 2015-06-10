package com.github.diegopacheco.sandbox.java.cassandra.springdata;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;

import com.github.diegopacheco.sandbox.java.cassandra.springdata.simple.Person;

public class SimpleService {
	
	public SimpleService() {}
	
	@Autowired
	private CassandraOperations co;
	
	public void insert(){
		String cqlIngest = "insert into person (id, name, age) values (?, ?, ?)";
		List<Object> person1 = new ArrayList<Object>();
		person1.add("10000");
		person1.add("David");
		person1.add(40);
		List<Object> person2 = new ArrayList<Object>();
		person2.add("10001");
		person2.add("Roger");
		person2.add(65);
		List<List<?>> people = new ArrayList<List<?>>();
		people.add(person1);
		people.add(person2);
		co.ingest(cqlIngest, people);
		System.out.println("People added: " + people);
	}
	
	public void select(){
		String cqlAll = "select * from person";
		List<Person> results = co.select(cqlAll, Person.class);
		for (Person p : results) {
			System.out.println(String.format("Found People with Name [%s] for id [%s]", p.getName(), p.getId()));
		}
	}

	public void setCassandraOperations(CassandraOperations co) {
		this.co = co;
	}
	
}

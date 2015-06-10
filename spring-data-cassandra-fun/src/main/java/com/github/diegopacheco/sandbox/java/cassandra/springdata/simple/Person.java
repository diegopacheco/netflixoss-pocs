package com.github.diegopacheco.sandbox.java.cassandra.springdata.simple;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

@Table(value="person")
public class Person {

	 @PrimaryKey
	 private String id;
	 
	 @Column(value="name")
	 private String name;
	 
	 @Column(value="age")
	 private int age;

	 public Person(String id, String name, int age) {
	  this.id = id;
	  this.name = name;
	  this.age = age;
	 }

	 public String getId() {
	  return id;
	 }

	 public String getName() {
	  return name;
	 }

	 public int getAge() {
	  return age;
	 }

	 @Override
	 public String toString() {
	  return "Person [id=" + id + ", name=" + name + ", age=" + age + "]";
	 }
	
	
}

package com.github.diegopacheco.sandbox.java.cassandra.springdata;

import java.util.stream.Stream;

import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.github.diegopacheco.sandbox.java.cassandra.springdata.simple.Person;

public interface PersonRepository extends CrudRepository<Person, String> {
	@Query("select * from person")
	Stream<Person> findAllByPachecoStream();
}

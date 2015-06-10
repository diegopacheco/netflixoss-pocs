package com.github.diegopacheco.sandbox.java.cassandra.springdata.simple;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;

public class CassandraApp {
	private static Cluster cluster;
	private static Session session;

	public static void main(String[] args) {
		try {

			cluster = Cluster.builder().addContactPoints(InetAddress.getByName("localhost")).build();
			session = cluster.connect("mykeyspace");

			CassandraOperations cassandraOps = new CassandraTemplate(session);
			cassandraOps.insert(new Person("1234567890", "David", 40));

			Select s = QueryBuilder.select().from("person");
			s.where(QueryBuilder.eq("id", "1234567890"));

			System.out.println("ID : " + cassandraOps.queryForObject(s, String.class).toString());
			cassandraOps.truncate("person");

		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

}

package com.github.diegopacheco.java.sanbox.cassandradatastax.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.HostDistance;
import com.datastax.driver.core.PoolingOptions;
import com.datastax.driver.core.ProtocolOptions;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SocketOptions;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;

public class MainApp {
	public static void main(String[] args) {
			
		Logger logger = LoggerFactory.getLogger("com.github.diegopacheco.java.sanbox.cassandradatastax.main.MainApp");
		logger.info("Hello world logback :-) ");
		 
		Cluster cluster = clusterConnect();
		Session session = cluster.connect("datastax_mapper_test");
		logger.info("Connected to Cassandra Cluster: " + cluster + " on Session: " + session);
		
		MappingManager manager  = new MappingManager(session);
		Mapper<User> userMapper = manager.mapper(User.class);
		
		User u1 = new User("Diego", "diegoSQN@gmail.com",1984);
		userMapper.save(u1);
		logger.info("User stored in cassandra: " + u1);
		
		UserAccessor userAccessor = manager.createAccessor(UserAccessor.class);
		Result<User> users = userAccessor.getAll();
		logger.info("Users from accessor: " + users);
		
		for(User u : users){
			System.out.println(u);
		}
		
	}

	private static Cluster clusterConnect() {
		Builder builder = Cluster.builder();
		builder.addContactPoint("127.0.0.1");

		PoolingOptions pool  = new PoolingOptions();
		pool.setNewConnectionThreshold(HostDistance.LOCAL, 128);
		pool.setNewConnectionThreshold(HostDistance.REMOTE, 128);
		
		pool.setCoreConnectionsPerHost(HostDistance.LOCAL, 2);
		pool.setMaxConnectionsPerHost(HostDistance.LOCAL, 10);
		pool.setCoreConnectionsPerHost(HostDistance.REMOTE, 2);
		pool.setMaxConnectionsPerHost(HostDistance.REMOTE, 10);
		
		Cluster cluster =  builder
						  .withPort(9042)	
						  .withPoolingOptions(pool)
						  .withSocketOptions(new SocketOptions().setTcpNoDelay(true))
						  .build();
		
		cluster.getConfiguration().
		getProtocolOptions().
		setCompression(ProtocolOptions.Compression.SNAPPY);
		
		return cluster;
	}
}

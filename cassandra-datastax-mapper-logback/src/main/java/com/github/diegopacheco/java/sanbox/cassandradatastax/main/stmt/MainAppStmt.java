package com.github.diegopacheco.java.sanbox.cassandradatastax.main.stmt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.HostDistance;
import com.datastax.driver.core.PoolingOptions;
import com.datastax.driver.core.ProtocolOptions;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SocketOptions;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.mapping.Result;

public class MainAppStmt {
	
	private static final Logger logger   = LoggerFactory.getLogger("com.github.diegopacheco.java.sanbox.cassandradatastax.main.MainApp");
	private static final Cluster cluster = clusterConnect();
	private static final Session session = cluster.connect("datastax_mapper_test");
	private static final Integer TOTAL_RUNS = 200; 
	
	private static void startup(){
		logger.info("Connected to Cassandra Cluster: " + cluster + " on Session: " + session);
	}
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		
		startup();
		
		final Result<User2> users = null;
		
		for(int i=0;i<=TOTAL_RUNS;i++){
			new Thread(new Runnable() {
				@Override
				public void run() {
					User2 u1 = new User2("Diego", "diegoSQN@gmail.com",1984);
					
					session.execute(QueryBuilder.insertInto("datastax_mapper_test","users")
							        .value("user_id", u1.getUserId())
							        .value("name", u1.getName())
							        .value("email", u1.getEmail())
							        .value("year", u1.getYear()));
					
					session.execute(
							QueryBuilder
							.select()
							.from("datastax_mapper_test","users"));
					
				}
			}).start();
			 
		}
		logger.info("Users from accessor: " + users);
		
		if (users!=null){
			for(User2 u : users){
				System.out.println(u);
			}
		}
	}

	private static Cluster clusterConnect() {
		Builder builder = Cluster.builder();
		builder.addContactPoint("127.0.0.1");

		PoolingOptions pool  = new PoolingOptions();
		pool.setMaxSimultaneousRequestsPerConnectionThreshold(HostDistance.LOCAL, 128);
		pool.setMaxSimultaneousRequestsPerConnectionThreshold(HostDistance.REMOTE, 128);
		pool.setCoreConnectionsPerHost(HostDistance.LOCAL, 2);
		pool.setCoreConnectionsPerHost(HostDistance.REMOTE, 2);
		pool.setMaxConnectionsPerHost(HostDistance.LOCAL, 10);
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

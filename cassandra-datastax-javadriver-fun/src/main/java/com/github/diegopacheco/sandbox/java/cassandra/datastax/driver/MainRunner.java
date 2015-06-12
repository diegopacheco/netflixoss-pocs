package com.github.diegopacheco.sandbox.java.cassandra.datastax.driver;

import static com.datastax.driver.core.querybuilder.QueryBuilder.bindMarker;
import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;
import static com.datastax.driver.core.querybuilder.QueryBuilder.insertInto;
import static com.datastax.driver.core.querybuilder.QueryBuilder.select;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.HostDistance;
import com.datastax.driver.core.PoolingOptions;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ProtocolOptions;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SocketOptions;
import com.datastax.driver.core.exceptions.InvalidQueryException;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

public class MainRunner {
	
	private static String contactPointsStr = "127.0.0.1";
	private static String keyspace = "datastax_keyspace";
	private static Cluster cluster;
	 
	public static void main(String[] args) {
		
		PoolingOptions pools = new PoolingOptions();
        pools.setMaxSimultaneousRequestsPerConnectionThreshold(HostDistance.LOCAL, 128);
        pools.setCoreConnectionsPerHost(HostDistance.LOCAL, 2);
        pools.setMaxConnectionsPerHost(HostDistance.LOCAL, 10);
        pools.setCoreConnectionsPerHost(HostDistance.REMOTE, 2);
        pools.setMaxConnectionsPerHost(HostDistance.REMOTE, 10);
		
		cluster = Cluster.builder().
				  addContactPoints(contactPointsStr).
				  withPoolingOptions(pools).
				  withSocketOptions(new SocketOptions().setTcpNoDelay(true)).
				  build();
		
		cluster.getConfiguration().
				getProtocolOptions().
				setCompression(ProtocolOptions.Compression.SNAPPY);
		
		Session session = cluster.connect();
		 
	    try {
            session.execute("USE " + keyspace);
        } catch (InvalidQueryException e) {
            createDatastaxSchema(session);
        } 
	    
	    minitor(session);
	    insertDatastax(session);
	    //benchFor(session,5000);
	    benchAsyncFor(session,5000);
        clean(session);
	}

	private static void minitor(Session session) {
		ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(1);
	    scheduled.scheduleAtFixedRate(new Runnable() {
	        @Override
	        public void run() {
	            Session.State state = session.getState();
	            for (Host host : state.getConnectedHosts()) {
	                int connections = state.getOpenConnections(host);
	                int inFlightQueries = state.getInFlightQueries(host);
	                System.out.printf("%s connections=%d current load=%d max load=%d%n",
	                    host, connections, inFlightQueries, connections * 128);
	            }
	        }
	    }, 1, 1, TimeUnit.SECONDS);
	}

	private static void createDatastaxSchema(Session session) {
		session.execute("CREATE KEYSPACE " + keyspace +
		    " with replication = {'class': 'SimpleStrategy', 'replication_factor' : 1}");

		session.execute("CREATE TABLE " + keyspace + "." + "test" + " (" +
		    "id text PRIMARY KEY," +
		    "descricao text" +
		    ")");
	}

	private static void clean(Session session) {
		session.execute("DROP KEYSPACE " + keyspace);
		System.exit(0);
	}

	private static void benchFor(Session session,int num) {
		final CyclicBarrier gate = new CyclicBarrier(num+1);
		
		for(int i=0;i<=num;i++){
	    
			new Thread(new Runnable(){
				@Override
				public void run() {
					
					try {
						gate.await();
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
					
					for(int i=0;i<=100000;i++){
						try {
							selectDatastax(session);
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					}
				}
			}).start();
			
	    }
		
		try {
			gate.await();
			System.out.println("all threads started");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private static void benchAsyncFor(Session session,int num) {
		final CyclicBarrier gate = new CyclicBarrier(num+1);
		
		for(int i=0;i<=num;i++){
	    
			new Thread(new Runnable(){
				@Override
				public void run() {
					
					try {
						gate.await();
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
					
					for(int i=0;i<=100000;i++){
						try {
							selectAsyncDatastax(session);
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					}
				}
			}).start();
			
	    }
		
		try {
			gate.await();
			System.out.println("all threads started");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void insertDatastax(Session session) {
		PreparedStatement insertStatement = session.prepare(insertInto(keyspace, "test")
	            .value("id", bindMarker())
	            .value("descricao", bindMarker())
	    );
	   
	    BoundStatement statement = new BoundStatement(insertStatement);
        statement.setString(0, "diego");
        statement.setString(1, "descricao diego");
        session.execute(statement);
	}

	private static void selectDatastax(Session session) {
		
		PreparedStatement retrieveStatement = session.prepare(
				select()
	            .from(keyspace, "test")
	            .where(eq("id", bindMarker())));
		
	    BoundStatement statement2 = new BoundStatement(retrieveStatement);
	    statement2.setString(0, "diego");
        ResultSet result = session.execute(statement2);
        //System.out.println(result);
        
        for(Row input: result){
        	String data = input.getString("id") + " - " + input.getString("descricao");
        	//System.out.println(data);
        }
	}
	
	private static void selectAsyncDatastax(Session session) {
		
		PreparedStatement retrieveStatement = session.prepare(
				select()
	            .from(keyspace, "test")
	            .where(eq("id", bindMarker())));
		
	    BoundStatement statement2 = new BoundStatement(retrieveStatement);
	    statement2.setString(0, "diego");
	    ListenableFuture<ResultSet> result = session.executeAsync(statement2);
	    
	    Futures.addCallback(result, new FutureCallback<ResultSet>() {
	    	  public void onSuccess(ResultSet rs) {
	    		  //System.out.println("Done: " + rs);
	    		  for(Row input: rs){
	    			  String data = input.getString("id") + " - " + input.getString("descricao");
	    			 // System.out.println(data);
	    		  }
	    	  }
	    	  public void onFailure(Throwable e) {
	    	      throw new RuntimeException(e);
	    	  }
	    });
       
	}
}

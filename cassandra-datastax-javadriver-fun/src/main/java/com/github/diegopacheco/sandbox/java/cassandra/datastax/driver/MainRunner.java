package com.github.diegopacheco.sandbox.java.cassandra.datastax.driver;

import static com.datastax.driver.core.querybuilder.QueryBuilder.bindMarker;
import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;
import static com.datastax.driver.core.querybuilder.QueryBuilder.insertInto;
import static com.datastax.driver.core.querybuilder.QueryBuilder.select;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.HostDistance;
import com.datastax.driver.core.PoolingOptions;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SocketOptions;
import com.datastax.driver.core.exceptions.InvalidQueryException;

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
		
		Session session = cluster.connect();
		 
	    try {
            session.execute("USE " + keyspace);
        } catch (InvalidQueryException e) {
            createDatastaxSchema(session);
        } 
	    
	    insertDatastax(session);
	    benchFor(session,10);
        clean(session);
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
		for(int i=0;i<=num;i++){
	    	new Thread(new Runnable(){
				@Override
				public void run() {
					selectDatastax(session);
				}
			}).start();
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
        System.out.println(result);
        
        for(Row input: result){
        	System.out.println(input.getString("id") + " - " + input.getString("descricao"));
        }
	}
}

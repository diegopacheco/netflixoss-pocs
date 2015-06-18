package com.github.diegopacheco.sandbox.java.achilles;

import info.archinnov.achilles.persistence.PersistenceManager;
import info.archinnov.achilles.persistence.PersistenceManagerFactory;
import info.archinnov.achilles.persistence.PersistenceManagerFactory.PersistenceManagerFactoryBuilder;
import info.archinnov.achilles.type.OptionsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.HostDistance;
import com.datastax.driver.core.PoolingOptions;
import com.datastax.driver.core.SocketOptions;
import com.datastax.driver.core.exceptions.InvalidQueryException;

public class AchillesMain {
	public static void main(String[] args) {
		
		String contactPointsStr = "127.0.0.1";
		
		PoolingOptions pools = new PoolingOptions();
        pools.setMaxSimultaneousRequestsPerConnectionThreshold(HostDistance.LOCAL, 128);
        pools.setCoreConnectionsPerHost(HostDistance.LOCAL, 2);
        pools.setMaxConnectionsPerHost(HostDistance.LOCAL, 10);
        pools.setCoreConnectionsPerHost(HostDistance.REMOTE, 2);
        pools.setMaxConnectionsPerHost(HostDistance.REMOTE, 10);
		
	    Cluster cluster = Cluster.builder().
	    		 addContactPoints(contactPointsStr).
				 withPoolingOptions(pools).
				 withSocketOptions(new SocketOptions().setTcpNoDelay(true)).
	    		 build();
		 
	    try {
	    	cluster.connect().execute("USE achilles_keyspace");
        } catch (InvalidQueryException e) {
        	
        	cluster.connect().execute("CREATE KEYSPACE achilles_keyspace " +
        		    " with replication = {'class': 'SimpleStrategy', 'replication_factor' : 1}");
        	
        	cluster.connect().execute("CREATE TABLE achilles_keyspace.users" + " (" +
        		    "id long PRIMARY KEY," +
        		    "firstname text," +
        		    "lastname text" +
        		    ")");
        } 
	    
	    
	    PersistenceManagerFactory persistenceManagerFactory = PersistenceManagerFactoryBuilder
	        .builder(cluster)
	        .withEntityPackages("com.github.diegopacheco.sandbox.java.achilles")
	        .withKeyspaceName("achilles_keyspace")
	        .forceTableCreation(true).build();
		
	    PersistenceManager manager = persistenceManagerFactory.createPersistenceManager();
	    
	    User user = new User();
	    user.setId(1L);
	    user.setFirstname("DuyHai");
	    user.setLastname("DOAN");
	    
	    // Save user
	    manager.insert(user);
	    
	    User foundUser = manager.find(User.class,1L);
	    User proxy = manager.forUpdate(User.class,1L);
	    proxy.setLastname(" mod"); 
	    //manager.update(foundUser);
	    
	    List<User> foundRange = manager.sliceQuery(User.class)
	            .forSelect()
	            .withPartitionComponents(1L)
	            //.fromClusterings(UUID.randomUUID())
	            //.toClusterings(UUID.randomUUID())
	            .limit(10)
	            .fromInclusiveToExclusiveBounds()
	            .orderByAscending()
	            .get();
	    
	    System.out.println(foundRange);
	    
	}
}

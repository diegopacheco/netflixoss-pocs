package com.github.diegopacheco.netflixoss.pocs.astyanax.cass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Supplier;
import com.netflix.astyanax.AstyanaxContext;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.connectionpool.Host;
import com.netflix.astyanax.connectionpool.NodeDiscoveryType;
import com.netflix.astyanax.connectionpool.OperationResult;
import com.netflix.astyanax.connectionpool.impl.ConnectionPoolConfigurationImpl;
import com.netflix.astyanax.connectionpool.impl.ConnectionPoolType;
import com.netflix.astyanax.connectionpool.impl.CountingConnectionPoolMonitor;
import com.netflix.astyanax.cql.CqlStatementResult;
import com.netflix.astyanax.impl.AstyanaxConfigurationImpl;
import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.model.Rows;
import com.netflix.astyanax.serializers.StringSerializer;
import com.netflix.astyanax.thrift.ThriftFamilyFactory;

public class Main {
	
	private static final Logger logger = LoggerFactory.getLogger(Main.class);
	
	private final AstyanaxContext<Keyspace> ctx;
	private final Keyspace bootKeyspace;
	
	private final Integer THRIFT_PORT = 9160;
	private final String BOOT_CLUSTER = "Test Cluster";
	private final String KS_NAME = "cluster_test";
	private final ColumnFamily<String, String> CF_TEST = new ColumnFamily<String, String>("test", StringSerializer.get(), StringSerializer.get(), StringSerializer.get());
	private final String CASSANDRA_SEEDS = System.getProperty("CASS_SEEDS","");
	
	public Main() {
		ctx = initWithThriftDriverWithExternalHostsSupplier();
		ctx.start();
		bootKeyspace = ctx.getClient();
	}
	
	private Supplier<List<Host>> getSupplier() {
		return new Supplier<List<Host>>() {
			@Override
			public List<Host> get() {
				List<Host> hosts = new ArrayList<Host>();
				List<String> cassHostnames = new ArrayList<String>(Arrays.asList(StringUtils.split(CASSANDRA_SEEDS, ",")));
				if (cassHostnames.size() == 0)
					throw new RuntimeException("Cassandra Host Names can not be blank. At least one host is needed. Please use getCassandraSeeds() property.");
				for (String cassHost : cassHostnames) {
					logger.info("Adding Cassandra Host = {}", cassHost);
					hosts.add(new Host(cassHost, THRIFT_PORT));
				}
				return hosts;
			}
		};
	}
	
	private AstyanaxContext<Keyspace> initWithThriftDriverWithExternalHostsSupplier() {
		logger.info("BOOT_CLUSTER = {}, KS_NAME = {}", BOOT_CLUSTER, KS_NAME);
		return new AstyanaxContext.Builder().forCluster(BOOT_CLUSTER).forKeyspace(KS_NAME)
		    .withAstyanaxConfiguration(new AstyanaxConfigurationImpl().setDiscoveryType(NodeDiscoveryType.DISCOVERY_SERVICE)
		        .setConnectionPoolType(ConnectionPoolType.ROUND_ROBIN))
		    .withConnectionPoolConfiguration(
		    		new ConnectionPoolConfigurationImpl("MyConnectionPool")
		    		.setMaxConnsPerHost(3)
		        .setPort(THRIFT_PORT))
		    .withHostSupplier(getSupplier())
		    	 .withConnectionPoolMonitor(new CountingConnectionPoolMonitor())
		    .buildKeyspace(ThriftFamilyFactory.getInstance());
	}

	public static void main(String[] args) throws Throwable {
		Main m = new Main();
		System.out.println("Connected to CASS... ");
		
		while(true) {
			try {
				Rows<String, String> result = m.bootKeyspace.prepareQuery(m.CF_TEST)
				    .getAllRows()
				    .execute().getResult();
				
				System.out.print("Cassandra Result: ");
				result.forEach( r -> System.out.print(r.getKey() + " "));
				System.out.print("\n");
				
			}catch(Exception e) {
				System.out.println("Error: " + e);
			}finally{
				Thread.sleep(2000L);
			}
		}
		
	}
}

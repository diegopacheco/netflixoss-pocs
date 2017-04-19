package com.github.diegopacheco.sandbox.java.netflixoss.dyno.msa.dynomite.hack;

import org.slf4j.Logger;

import com.netflix.discovery.DiscoveryClient;
import com.netflix.dyno.connectionpool.ConnectionPool;
import com.netflix.dyno.connectionpool.ConnectionPoolConfiguration;
import com.netflix.dyno.connectionpool.ConnectionPoolMonitor;
import com.netflix.dyno.connectionpool.HostSupplier;
import com.netflix.dyno.connectionpool.exception.DynoConnectException;
import com.netflix.dyno.connectionpool.exception.NoAvailableHostsException;
import com.netflix.dyno.connectionpool.impl.ConnectionPoolConfigurationImpl;
import com.netflix.dyno.connectionpool.impl.ConnectionPoolImpl;
import com.netflix.dyno.connectionpool.impl.lb.HttpEndpointBasedTokenMapSupplier;
import com.netflix.dyno.contrib.ArchaiusConnectionPoolConfiguration;
import com.netflix.dyno.contrib.DynoCPMonitor;
import com.netflix.dyno.contrib.DynoOPMonitor;
import com.netflix.dyno.contrib.EurekaHostsSupplier;
import com.netflix.dyno.jedis.DynoDualWriterClient;
import com.netflix.dyno.jedis.DynoJedisClient;
import com.netflix.dyno.jedis.JedisConnectionFactory;

import redis.clients.jedis.Jedis;

/**
 * Force dual write to work.
 * 
 * @author diegopacheco
 *
 */
public class SimpleDualWriteDynoJedisClient extends DynoJedisClient{
	
	private static final Logger Logger = org.slf4j.LoggerFactory.getLogger(SimpleDualWriteDynoJedisClient.class);
	
	public SimpleDualWriteDynoJedisClient(String name, 
										  String clusterName, ConnectionPool<Jedis> pool,
										  DynoOPMonitor operationMonitor, 
										  ConnectionPoolMonitor cpMonitor) {
		super(name, clusterName, pool, operationMonitor, cpMonitor);
	}
	
	public static class Builder extends DynoJedisClient.Builder {
		
		 	private String appName;
	        private String clusterName;
	        private ConnectionPoolConfigurationImpl cpConfig;
	        private ConnectionPoolConfigurationImpl cpConfigDualWrite;
	        private HostSupplier hostSupplier;
	        private DiscoveryClient discoveryClient;
	        private String dualWriteClusterName;
	        private HostSupplier dualWriteHostSupplier;
	        private DynoDualWriterClient.Dial dualWriteDial;
	        private ConnectionPoolMonitor cpMonitor;
	        private DualWriteConnectionPoolImpl<Jedis> jedisConnection;

	        public Builder() {
	        }

	        public Builder withApplicationName(String applicationName) {
	            appName = applicationName;
	            return this;
	        }

	        public Builder withDynomiteClusterName(String cluster) {
	            clusterName = cluster;
	            return this;
	        }

	        public Builder withCPConfig(ConnectionPoolConfigurationImpl config) {
	            cpConfig = config;
	            return this;
	        }

	        public Builder withHostSupplier(HostSupplier hSupplier) {
	            hostSupplier = hSupplier;
	            return this;
	        }


	        public Builder withDiscoveryClient(DiscoveryClient client) {
	            discoveryClient = client;
	            return this;
	        }

	        public Builder withDualWriteClusterName(String dualWriteCluster) {
	            dualWriteClusterName = dualWriteCluster;
	            return this;
	        }

	        public Builder withDualWriteHostSupplier(HostSupplier dualWriteHostSupplier) {
	            this.dualWriteHostSupplier = dualWriteHostSupplier;
	            return this;
	        }

	        public Builder withDualWriteDial(DynoDualWriterClient.Dial dial) {
	            this.dualWriteDial = dial;
	            return this;
	        }

	        public Builder withConnectionPoolMonitor(ConnectionPoolMonitor cpMonitor){
	            this.cpMonitor = cpMonitor;
	            return this;
	        }
	        
	        public Builder withDualWriteCPConfig(ConnectionPoolConfigurationImpl config) {
	        	cpConfigDualWrite = config;
	            return this;
	        }
	        
	        public Builder clone(){ 
	        	Builder newBuilder = new Builder();
	        	newBuilder.appName = this.appName;
	        	newBuilder.clusterName = this.clusterName;
	        	newBuilder.cpConfig = this.cpConfig;
	        	newBuilder.cpMonitor = this.cpMonitor;
	        	newBuilder.hostSupplier = this.hostSupplier;
	        	newBuilder.discoveryClient = this.discoveryClient;
	        	newBuilder.dualWriteClusterName = this.dualWriteClusterName;
	        	newBuilder.dualWriteDial = this.dualWriteDial;
	        	newBuilder.dualWriteHostSupplier = this.dualWriteHostSupplier;
	        	return newBuilder;
	        }

			public DynoJedisClient build() {
	            assert (appName != null);
	            assert (clusterName != null);

	            if (cpConfig == null) {
	                cpConfig = new ArchaiusConnectionPoolConfiguration(appName);
	                Logger.info("Dyno Client runtime properties: " + cpConfig.toString());
	            }

	            DynoJedisClient client = buildDynoJedisClient();
	            return client;
	        }
	        
	        @SuppressWarnings({ "unchecked", "rawtypes" })
	    	public DynoJedisClient buildForDualWrite() {
		            if (cpConfig == null) {
		                cpConfig = new ArchaiusConnectionPoolConfiguration(appName);
		                Logger.info("Dyno Client runtime properties: " + cpConfig.toString());
		            }
	
		            DynoJedisClient client = null;
		            if (cpConfig.isDualWriteEnabled()) {
		            	client = build();
		            	
		            	try {
							Builder shadowBuilder = this.clone();
							shadowBuilder.withHostSupplier(this.dualWriteHostSupplier);
							shadowBuilder.withDynomiteClusterName(this.clusterName + "_shadow");
							shadowBuilder.withApplicationName(this.appName + "_shadow");
							shadowBuilder.withConnectionPoolMonitor((shadowBuilder.cpMonitor == null) ? new DynoCPMonitor(shadowBuilder.appName) : shadowBuilder.cpMonitor);
							shadowBuilder.cpConfig = this.cpConfigDualWrite;
							DynoJedisClient shadowClient =  shadowBuilder.build();
							
							((DualWriteConnectionPoolImpl)client.getConnPool()).setFullShadowClient(shadowClient);
							((DualWriteConnectionPoolImpl)client.getConnPool()).setShadow(shadowBuilder.jedisConnection);
							
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
		            } else {
		            	client = build();
		            }
		            return client;
	    	}

	        private DynoDualWriterClient buildDynoDualWriterClient() {
	            ConnectionPoolConfigurationImpl shadowConfig = new ConnectionPoolConfigurationImpl(cpConfig);
	            Logger.info("Dyno Client Shadow Config runtime properties: " + shadowConfig.toString());

	            // Ensure that if the shadow cluster is down it will not block client application startup
	            shadowConfig.setFailOnStartupIfNoHosts(false);

	           

	            HostSupplier shadowSupplier = null;
	            if (dualWriteHostSupplier == null) {
	                if (hostSupplier != null && hostSupplier instanceof EurekaHostsSupplier) {
	                    EurekaHostsSupplier eurekaSupplier = (EurekaHostsSupplier) hostSupplier;
	                    shadowSupplier = EurekaHostsSupplier.newInstance(shadowConfig.getDualWriteClusterName(), eurekaSupplier);
	                } else if (discoveryClient != null) {
	                    shadowSupplier = new EurekaHostsSupplier(shadowConfig.getDualWriteClusterName(), discoveryClient);
	                } else {
	                    throw new DynoConnectException("HostSupplier for DualWrite cluster is REQUIRED if you are not " +
	                        "using EurekaHostsSupplier implementation or using a DiscoveryClient");
	                }
	            } else {
	                shadowSupplier = dualWriteHostSupplier;
	            }

	            shadowConfig.withHostSupplier(shadowSupplier);

	            setLoadBalancingStrategy(shadowConfig);

	            String shadowAppName = shadowConfig.getName();
	            DynoCPMonitor shadowCPMonitor = new DynoCPMonitor(shadowAppName);
	            DynoOPMonitor shadowOPMonitor = new DynoOPMonitor(shadowAppName);

	            JedisConnectionFactory connFactory = new JedisConnectionFactory(shadowOPMonitor);

	            final ConnectionPoolImpl<Jedis> shadowPool =
	                    startConnectionPool(shadowAppName, connFactory, shadowConfig, shadowCPMonitor);

	            // Construct a connection pool with the shadow cluster settings
	            DynoJedisClient shadowClient = new DynoJedisClient(shadowAppName, dualWriteClusterName, shadowPool, shadowOPMonitor, shadowCPMonitor);

	            // Construct an instance of our DualWriter client
	            DynoOPMonitor opMonitor = new DynoOPMonitor(appName);
	            ConnectionPoolMonitor cpMonitor = (this.cpMonitor == null) ? new DynoCPMonitor(appName) : this.cpMonitor;

	            final ConnectionPoolImpl<Jedis> pool = createConnectionPool(appName, opMonitor, cpMonitor);

	            if (dualWriteDial != null) {
	                if (shadowConfig.getDualWritePercentage() > 0) {
	                    dualWriteDial.setRange(shadowConfig.getDualWritePercentage());
	                }

	                return new DynoDualWriterClient(appName, clusterName, pool, opMonitor, cpMonitor, shadowClient, dualWriteDial);
	            } else {
	                return new DynoDualWriterClient(appName, clusterName, pool, opMonitor, cpMonitor, shadowClient);
	            }
	        }


	        private DynoJedisClient buildDynoJedisClient() {
	            DynoOPMonitor opMonitor = new DynoOPMonitor(appName);
	            ConnectionPoolMonitor cpMonitor = (this.cpMonitor == null) ? new DynoCPMonitor(appName) : this.cpMonitor;
	            
	            final DualWriteConnectionPoolImpl<Jedis> pool = createConnectionPool(appName, opMonitor, cpMonitor);
	            this.jedisConnection = pool;

	            return new DynoJedisClient(appName, clusterName, pool, opMonitor, cpMonitor);
	        }

	        private DualWriteConnectionPoolImpl<Jedis> createConnectionPool(String appName, DynoOPMonitor opMonitor, ConnectionPoolMonitor cpMonitor) {

	            if (hostSupplier == null) {
	                if (discoveryClient == null) {
	                    throw new DynoConnectException("HostSupplier not provided. Cannot initialize EurekaHostsSupplier " +
	                            "which requires a DiscoveryClient");
	                } else {
	                    hostSupplier = new EurekaHostsSupplier(clusterName, discoveryClient);
	                }
	            }

	            cpConfig.withHostSupplier(hostSupplier);

	            setLoadBalancingStrategy(cpConfig);

	            JedisConnectionFactory connFactory = new JedisConnectionFactory(opMonitor);

	            return startConnectionPool(appName, connFactory, cpConfig, cpMonitor);
	        }

	        private DualWriteConnectionPoolImpl<Jedis> startConnectionPool(String appName, JedisConnectionFactory connFactory,
	                                                              ConnectionPoolConfigurationImpl cpConfig,
	                                                              ConnectionPoolMonitor cpMonitor) {

	            final DualWriteConnectionPoolImpl<Jedis> pool = new DualWriteConnectionPoolImpl<Jedis>(connFactory, cpConfig, cpMonitor);

	            try {
	                Logger.info("Starting connection pool for app " + appName);

	                pool.start().get();

	                Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
	                    @Override
	                    public void run() {
	                        pool.shutdown();
	                    }
	                }));
	            } catch (NoAvailableHostsException e) {
	                if (cpConfig.getFailOnStartupIfNoHosts()) {
	                    throw new RuntimeException(e);
	                }

	                Logger.warn("UNABLE TO START CONNECTION POOL -- IDLING");

	                pool.idle();
	            } catch (Exception e) {
	                throw new RuntimeException(e);
	            }

	            return pool;
	        }

	        private void setLoadBalancingStrategy(ConnectionPoolConfigurationImpl config) {
	            if (ConnectionPoolConfiguration.LoadBalancingStrategy.TokenAware == config.getLoadBalancingStrategy()) {
	                if (config.getTokenSupplier() == null) {
	                    Logger.warn("TOKEN AWARE selected and no token supplier found, using default HttpEndpointBasedTokenMapSupplier()");
	                    config.withTokenSupplier(new HttpEndpointBasedTokenMapSupplier());
	                }

	                if (config.getLocalRack() == null && config.localZoneAffinity()) {
	                    String warningMessage =
	                            "DynoJedisClient for app=[" + config.getName() + "] is configured for local rack affinity "+
	                                    "but cannot determine the local rack! DISABLING rack affinity for this instance. " +
	                                    "To make the client aware of the local rack either use " +
	                                    "ConnectionPoolConfigurationImpl.setLocalRack() when constructing the client " +
	                                    "instance or ensure EC2_AVAILABILTY_ZONE is set as an environment variable, e.g. " +
	                                    "run with -DEC2_AVAILABILITY_ZONE=us-east-1c";
	                    config.setLocalZoneAffinity(false);
	                    Logger.warn(warningMessage);
	                }
	            }
	        }

	    }
	
}

package com.github.diegopacheco.sandbox.java.jedis.bench.dyno.netflix;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.EurekaInstanceConfig;
import com.netflix.appinfo.MyDataCenterInstanceConfig;
import com.netflix.discovery.DefaultEurekaClientConfig;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.EurekaClientConfig;
import com.netflix.dyno.contrib.ArchaiusConnectionPoolConfiguration;
import com.netflix.dyno.jedis.DynoJedisClient;

public class DynoClusterJedisEurekaDiscoveryMainBench {
private static Logger log = LoggerFactory.getLogger(DynoClusterJedisMainBench.class);
	
	public static void main(String[] args) throws Throwable {
		for(int i=0;i<=2;i++){
			benchInsert(10);
			benchGet(10);
			
			benchInsert(100);
			benchGet(100);
			
			benchInsert(1000);
			benchGet(1000);
			
			benchInsert(10000);
			benchGet(10000);
			
			benchInsert(100000);
			benchGet(100000);
		}
		
		System.exit(0);
	}
	
	private static DynoJedisClient createCluster() throws Throwable {
		
//        System.setProperty("log4j.rootLogger", "ERROR");
//        BasicConfigurator.configure();
//        log.info("Logger intialized");
		
		EurekaInstanceConfig eurekaConfig = new MyDataCenterInstanceConfig();
		ApplicationInfoManager applicationInfoManager = ApplicationInfoManager.getInstance();
		applicationInfoManager.initComponent(eurekaConfig);
		
		EurekaClientConfig config = new DefaultEurekaClientConfig();
		DiscoveryClient discoveryClient = new DiscoveryClient(applicationInfoManager,config);
		
		Thread.sleep(60000L); // 60s
		
		DynoJedisClient dynoClient = new DynoJedisClient.Builder()
		.withApplicationName("MY_APP")
        .withDynomiteClusterName("PRANADYNOMITEOREGON1") // MY_CLUSTER
        .withDiscoveryClient(discoveryClient) 
        .withCPConfig( new ArchaiusConnectionPoolConfiguration("MY_APP")
        					.setPort(8101)
        					.setLocalDC("localdc")
        					.setMaxConnsPerHost(100) )
        .build();
		return dynoClient;
}
	
	private static void printBench(String msg,double init, double end){
		int seconds = (int) ((end - init) / 1000) % 60 ;
		System.out.println("TIME to " + msg + ": " + (end - init) + " ms - " + seconds + " s" );
	}

	private static void benchInsert(int n) throws Throwable {
		DynoJedisClient cluster = createCluster();
		
		double init = System.currentTimeMillis();
		for(int i=0;i<=(n-1);i++){
			cluster.set("key"+i, "value"+i);
		}
		double end = System.currentTimeMillis();
		printBench("Insert " + n + " IDS ",init,end);
	}
	
	private static void benchGet(int n) throws Throwable {
		DynoJedisClient cluster = createCluster();
		
		double init = System.currentTimeMillis();
		for(int i=0;i<=(n-1);i++){
			cluster.get("key"+i);
		}
		double end = System.currentTimeMillis();
		printBench("Get " + n + " IDS ",init,end);
	}
}

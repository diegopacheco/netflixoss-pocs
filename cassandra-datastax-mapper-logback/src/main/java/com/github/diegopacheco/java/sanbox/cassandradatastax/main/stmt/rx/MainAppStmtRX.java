package com.github.diegopacheco.java.sanbox.cassandradatastax.main.stmt.rx;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.HostDistance;
import com.datastax.driver.core.PoolingOptions;
import com.datastax.driver.core.ProtocolOptions;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SocketOptions;

public class MainAppStmtRX {
	
	private static final Cluster cluster = clusterConnect();
	private static final Session session = cluster.connect("datastax_mapper_test");
	private static final SessionBackpressureRunner sbr = new SessionBackpressureRunner(session);
	
	private static final Integer TOTAL_RUNS = 20000; 
	
	public static void main(String[] args) {
		
		Observable.just( Command.of("Hello"),Command.of("World"), Command.of("RX")  )
				.buffer(1)
        		.subscribe(new Subscriber<Object>() {
					public void onCompleted() {
						System.out.println("DONE ALL FINITO!");
					}
					public void onError(Throwable e) {
						System.out.println("Eror:" + e);
					}
					@SuppressWarnings("unchecked")
					public void onNext(Object c) {
						((List<Command>)c).get(0).run();
						System.out.println(c);							
					}
				});
		
		for(int i=0;i<=TOTAL_RUNS;i++){
			new Thread(new Runnable() {
				public void run() {
					sbr.run("go");
				}
			}).start();
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

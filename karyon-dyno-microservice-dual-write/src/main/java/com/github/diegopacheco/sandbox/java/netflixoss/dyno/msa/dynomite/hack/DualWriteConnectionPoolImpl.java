package com.github.diegopacheco.sandbox.java.netflixoss.dyno.msa.dynomite.hack;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.netflix.dyno.connectionpool.ConnectionFactory;
import com.netflix.dyno.connectionpool.ConnectionPoolConfiguration;
import com.netflix.dyno.connectionpool.ConnectionPoolMonitor;
import com.netflix.dyno.connectionpool.Operation;
import com.netflix.dyno.connectionpool.OperationResult;
import com.netflix.dyno.connectionpool.exception.DynoException;
import com.netflix.dyno.connectionpool.impl.ConnectionPoolImpl;
import com.netflix.dyno.jedis.DynoJedisClient;

/**
 * ConnectionPoolImpl that forces dual write.
 * 
 * @author diegopacheco
 *
 * @param <CL>
 */
public class DualWriteConnectionPoolImpl<CL> extends ConnectionPoolImpl<CL> {
	
	private ExecutorService executor = Executors.newFixedThreadPool(5);
	
	private DynoJedisClient fullShadowClient;
	private DualWriteConnectionPoolImpl<CL> shadow;
	
	public DualWriteConnectionPoolImpl(ConnectionFactory<CL> cFactory,
									   ConnectionPoolConfiguration cpConfig,
									   ConnectionPoolMonitor cpMon){
		super(cFactory, cpConfig, cpMon);
	}
	
	public <R> OperationResult<R> executeWithFailoverSuper(Operation<CL, R> op) throws DynoException {
		return super.executeWithFailover(op);
	}
	
	@Override
	public <R> OperationResult<R> executeWithFailover(Operation<CL, R> op) throws DynoException {
		try{
			 executor.execute(new Runnable() {
				@Override
				public void run() {
					shadow.executeWithFailoverSuper(op);
				}
			});
		}catch(Exception e){
			// do nothing
		}
		return executeWithFailoverSuper(op);
	}

	public DualWriteConnectionPoolImpl<CL> getShadow() {
		return shadow;
	}
	public void setShadow(DualWriteConnectionPoolImpl<CL> shadow) {
		this.shadow = shadow;
	}
	
	public DynoJedisClient getFullShadowClient() {
		return fullShadowClient;
	}
	public void setFullShadowClient(DynoJedisClient fullShadowClient) {
		this.fullShadowClient = fullShadowClient;
	}
	
}

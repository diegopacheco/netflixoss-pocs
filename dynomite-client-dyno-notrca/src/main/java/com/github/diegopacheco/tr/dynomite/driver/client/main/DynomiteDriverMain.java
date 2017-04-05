package com.github.diegopacheco.tr.dynomite.driver.client.main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.netflix.config.ConfigurationManager;
import com.netflix.dyno.connectionpool.Host;
import com.netflix.dyno.connectionpool.Host.Status;
import com.netflix.dyno.connectionpool.HostSupplier;
import com.netflix.dyno.connectionpool.TokenMapSupplier;
import com.netflix.dyno.connectionpool.impl.RetryNTimes;
import com.netflix.dyno.connectionpool.impl.lb.AbstractTokenMapSupplier;
import com.netflix.dyno.contrib.ArchaiusConnectionPoolConfiguration;
import com.netflix.dyno.jedis.DynoJedisClient;

/**

-Ddynomite.driver.seeds=172.18.0.101:8101:rack1:dc:100|172.18.0.102:8101:rack2:dc:100|172.18.0.103:8101:rack3:dc:100
-Ddynomite.driver.cluster.name=dyn_o_mite
-Ddynomite.driver.client.name=dyn_o_mite

 * 
 * @author diegopacheco
 *
 */
public class DynomiteDriverMain {
	
	private DynoJedisClient dynoClient;
	
	private static TokenMapSupplier toTokenMapSupplier(List<DynomiteNodeInfo> nodes){
		StringBuilder jsonSB = new StringBuilder("[");
		int count = 0;
		for(DynomiteNodeInfo node: nodes){
			jsonSB.append(" {\"token\":\""+ node.getTokens() 
			                + "\",\"hostname\":\"" + node.getServer() 
							+ "\",\"zone\":\"" +  node.getDc() 
							+ "\"} ");
			count++;
			if (count < nodes.size())
				jsonSB.append(" , ");
		}
		jsonSB.append(" ]\"");
		
	   final String json = jsonSB.toString();
	   TokenMapSupplier testTokenMapSupplier = new AbstractTokenMapSupplier() {
			    @Override
			    public String getTopologyJsonPayload(String hostname) {
			        return json;
			    }
				@Override
				public String getTopologyJsonPayload(Set<Host> activeHosts) {
					return json;
				}
		};
		return testTokenMapSupplier;
	}
	
	private static HostSupplier toHostSupplier(List<DynomiteNodeInfo> nodes){
		final List<Host> hosts = new ArrayList<Host>();
		
		for(DynomiteNodeInfo node: nodes){
			hosts.add(buildHost(node));
		}
		
		final HostSupplier customHostSupplier = new HostSupplier() {
		   @Override
		   public Collection<Host> getHosts() {
			   return hosts;
		   }
		};
		return customHostSupplier;
	}
	
	private static Host buildHost(DynomiteNodeInfo node){
		Host host = new Host(node.getServer(),8102,node.getDc());
		host.setStatus(Status.Up);
		return host;
	}
	
	
	public DynomiteDriverMain(){
		
		ConfigurationManager.getConfigInstance().setProperty("dynomite.driver.seeds", "172.18.0.101:8101:rack1:dc:100|172.18.0.102:8101:rack2:dc:100|172.18.0.103:8101:rack3:dc:100");
		ConfigurationManager.getConfigInstance().setProperty("dynomite.driver.cluster.name","dyn_o_mite");
		ConfigurationManager.getConfigInstance().setProperty("dynomite.driver.client.name","dyn_o_mite"); 
		ConfigurationManager.getConfigInstance().setProperty("dyno.dyn_o_mite.retryPolicy","RetryNTimes:3:true");
		
		List<DynomiteNodeInfo> nodes = DynomiteSeedsParser.parse(ConfigurationManager.getConfigInstance().getString("dynomite.driver.seeds",""));
		
		DynoJedisClient dynoClient = new DynoJedisClient.Builder()
				.withApplicationName(ConfigurationManager.getConfigInstance().getString("dynomite.driver.client.name",""))
	            .withDynomiteClusterName(ConfigurationManager.getConfigInstance().getString("dynomite.driver.cluster.name",""))
	            .withCPConfig( new ArchaiusConnectionPoolConfiguration(ConfigurationManager.getConfigInstance().getString("dynomite.driver.client.name",""))
	            					.withTokenSupplier(toTokenMapSupplier(nodes))
	            					.setMaxConnsPerHost(1)
                                    .setConnectTimeout(2000)
                                    .setRetryPolicyFactory(new RetryNTimes.RetryFactory(3,true))
	            )
	            .withHostSupplier(toHostSupplier(nodes))
	            .build();
		
		dynoClient.set("x","DriverWorks_v1.1.11-SNAPSHOT");
		
		this.dynoClient = dynoClient;
	}
	
	public DynoJedisClient getDynoClient() {
		return dynoClient;
	}

	public static void main(String[] args) throws Throwable {
		
		DynomiteDriverMain ddm = new DynomiteDriverMain();
		
		while(true){
			try{
				Thread.sleep(5000L);
				String result = ddm.getDynoClient().get("x");
				System.out.println("PURE dyno 1.5.7 - Result " + result + " - " + System.currentTimeMillis());
			}catch(Throwable t){
				System.out.println(t.getMessage());
			}
			
		}
		
	}
}

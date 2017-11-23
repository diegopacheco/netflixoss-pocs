package com.github.diegopacheco.netflixoss.dyno.hashalgo;

import com.github.diegopacheco.netflixoss.dyno.hashalgo.util.Stopwatch;
import com.netflix.dyno.connectionpool.Host;
import com.netflix.dyno.connectionpool.Host.Status;
import com.netflix.dyno.connectionpool.impl.hash.BinarySearchTokenMapper;
import com.netflix.dyno.connectionpool.impl.hash.Murmur3HashPartitioner;
import com.netflix.dyno.connectionpool.impl.lb.HostToken;

public class DynoMain {
	public static void main(String[] args) {
		
		BinarySearchTokenMapper bs = new BinarySearchTokenMapper(new Murmur3HashPartitioner());
		
		//
		// Parse from: http://localhost:8080/REST/v1/admin/cluster_describe
		//
		bs.addHostToken(new HostToken(1383429731L, new Host("host1","ip-100",8101,"us-west-2c","us-west-2", Status.Up)));
		bs.addHostToken(new HostToken(-1383429731L, new Host("host2","ip-200",8101,"us-west-2b","us-west-2", Status.Up))); 
		bs.addHostToken(new HostToken(3383429731L, new Host("host3","ip-300",8101,"us-west-2a","us-west-2", Status.Up))); 
		
		String key = "100";
		Long hashKey = bs.hash(key);
		HostToken ht = bs.getToken(hashKey);
		System.out.println("For key  : " + key);
		System.out.println("Key Hash : " + hashKey);
		System.out.println("Host Token Found == : " + ht);
		
		Stopwatch s = new Stopwatch();
		s.start();
		int total = 1000;
		int countNode1 = 0;
		int countNode2 = 0;
		int countNode3 = 0;
		for(int i=0; i<= total; i++){
			key = "key-100-"+i;
			hashKey = bs.hash(key);
			ht = bs.getToken(hashKey);
			System.out.printf("\nkey %s Hash %d Node %s", key,hashKey,ht.getHost().getHostName());
			switch(ht.getHost().getHostName()){
				case "host1": countNode1++;
				break;
				case "host2": countNode2++;
				break;
				case "host3": countNode3++;
				break;
			}
		}
		System.out.println("");
		System.out.printf("Node 1: [%d%%-%d], Node 2: [%d%%-%d], Node 3: [%d%%-%d] of total: [%d-100%%]\r", (countNode1*100)/total,countNode1, (countNode2*100)/total,countNode2, (countNode3*100)/total,countNode3, total);
		s.printExecutionTime();
		
		
	}
}

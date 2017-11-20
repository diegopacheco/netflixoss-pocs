package com.github.diegopacheco.netflixoss.dyno.hashalgo;

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
		bs.addHostToken(new HostToken(1383429731L, new Host("host2","ip-200",8101,"us-west-2b","us-west-2", Status.Up)));
		bs.addHostToken(new HostToken(1383429731L, new Host("host3","ip-300",8101,"us-west-2a","us-west-2", Status.Up)));
		
		String key = "100";
		Long hashKey = bs.hash(key);
		HostToken ht = bs.getToken(hashKey);
		
		System.out.println("For key  : " + key);
		System.out.println("Key Hash : " + hashKey);
		System.out.println("Host Token Found == : " + ht);
		
	}
}

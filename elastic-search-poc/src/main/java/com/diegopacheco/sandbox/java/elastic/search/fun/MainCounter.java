package com.diegopacheco.sandbox.java.elastic.search.fun;

import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;

public class MainCounter {
	public static void main(String[] args) {
		
		Node node = nodeBuilder().clusterName("elasticsearch").node();
		Client client = node.client();
		
		CountResponse response = client.prepareCount("customer")
		        .setQuery(termQuery("_type", "type1"))
		        .execute()
		        .actionGet();
		
		System.out.println(response.getCount());
	}
	
}

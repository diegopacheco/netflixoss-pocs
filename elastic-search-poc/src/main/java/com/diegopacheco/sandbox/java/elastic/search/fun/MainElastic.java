package com.diegopacheco.sandbox.java.elastic.search.fun;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.node.Node;

public class MainElastic {
	public static void main(String[] args) {
		
		Node node = nodeBuilder().clusterName("elasticsearch").node();
		Client client = node.client();
		
		SearchResponse response = client.prepareSearch("customer")
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setQuery(QueryBuilders.queryStringQuery("*"))
		        .setFrom(0).setSize(60).setExplain(true)
		        .execute()
		        .actionGet();
		System.out.println("Query For * : " + response);
		
		SearchResponse response2 = client.prepareSearch("customer")
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setQuery(QueryBuilders.queryStringQuery("name='John Doe 3'"))
		        .setFrom(0).setSize(60).setExplain(false)
		        .execute()
		        .actionGet();
		System.out.println("Query for name: " + response2);
		
	}
}

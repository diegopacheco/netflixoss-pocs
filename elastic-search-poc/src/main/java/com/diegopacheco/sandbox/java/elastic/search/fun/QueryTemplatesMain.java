package com.diegopacheco.sandbox.java.elastic.search.fun;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.action.indexedscripts.put.PutIndexedScriptResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;

public class QueryTemplatesMain {
	public static void main(String[] args) {
		
		Node node = nodeBuilder().clusterName("elasticsearch").node();
		Client client = node.client();
		
		Map<String, Object> template_params = new HashMap<>();
		template_params.put("param_name", "John Doe 3");
		
		SearchRequestBuilder sr = client.prepareSearch("customer")
						 		  .setTemplateParams(template_params);
		
		PutIndexedScriptResponse response = client.preparePutIndexedScript("mustache", "template_name",
					        "{\n" +
					        "    \"template\" : {\n" +
					        "        \"query\" : {\n" +
					        "            \"match\" : {\n" +
					        "                \"name\" : \"{{param_gender}}\"\n" +
					        "            }\n" +
					        "        }\n" +
					        "    }\n" +
					        "}").get();
		System.out.println(response);
	}
}

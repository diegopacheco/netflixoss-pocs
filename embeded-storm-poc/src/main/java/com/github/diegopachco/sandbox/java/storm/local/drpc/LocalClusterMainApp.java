package com.github.diegopachco.sandbox.java.storm.local.drpc;

import storm.trident.TridentTopology;
import storm.trident.operation.builtin.Count;
import storm.trident.operation.builtin.Debug;
import storm.trident.testing.Split;
import backtype.storm.Config;
import backtype.storm.LocalDRPC;
import backtype.storm.generated.StormTopology;
import backtype.storm.tuple.Fields;

public class LocalClusterMainApp {

	public static StormTopology buildTopology(LocalDRPC localDRPC) {
		TridentTopology topology = new TridentTopology();
		topology.newDRPCStream("word-counter", localDRPC)
				.each(new Fields("args"), new Split(), new Fields("word"))
				.groupBy(new Fields("word"))
				.aggregate(new Fields("word"), new Count(), new Fields("count"))
				.each(new Fields("word", "count"), new Debug());
		return topology.build();
	}

	public static void startCluster() {
		LocalDRPC localDRPC = new LocalDRPC();
		Config conf = new Config();
		conf.setDebug(false);
		backtype.storm.LocalCluster localCluster = new backtype.storm.LocalCluster();
		localCluster.submitTopology("drpc-word-counter-topology", conf, buildTopology(localDRPC) );
		String result = localDRPC.execute("word-counter", "Quantas Palavras Quantas");
		System.out.println(result);
		localDRPC.shutdown();
	}

	public static void main(String[] args) {
		LocalClusterMainApp.startCluster();
	}

}

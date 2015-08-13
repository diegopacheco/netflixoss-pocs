package com.github.diegopachco.sandbox.java.storm.localcluster;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;

public class StormMainRunner {
	public static void main(String[] args) throws Exception{
		
	    TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("word-reader", new WordReaderBolt());
        builder.setBolt("word-normalizer", new WordNormalizerBolt(), 3).shuffleGrouping("word-reader");
        builder.setBolt("word-counter", new WordCounterBolt(), 3).shuffleGrouping("word-normalizer");

        Config conf = new Config();
        conf.put("wordsFile", "D:/diego/github/diego.pacheco/netflixoss-pocs/embeded-storm-poc/src/main/resources/myfile.txt");
        conf.setNumWorkers(4);
        conf.setDebug(true);
        conf.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1);

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("My-first-topology", conf, builder.createTopology());
        Thread.sleep(20000);
        cluster.shutdown();
		
	}
}

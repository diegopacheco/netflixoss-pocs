package com.github.diegopachco.sandbox.java.storm.localcluster;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

@SuppressWarnings("rawtypes")
public class WordNormalizerBolt implements IRichBolt {

	private static final long serialVersionUID = 1L;
	private OutputCollector collector;

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        collector =  outputCollector;
    }
    
	@Override
	@SuppressWarnings("unchecked")
	public void execute(Tuple tuple) {
        System.out.println("**** word normalizing **** " + tuple.getString(0));
        String sentence = tuple.getString(0);
        System.out.println(sentence);
        String[] words = sentence.split(" ");
        for (String word : words) {
            if (!word.isEmpty()) {
                word.toLowerCase();
                List a = new ArrayList();
                a.add(tuple);
                collector.emit(a, new Values(word));
            }
        }
        collector.ack(tuple);
    }

    @Override
    public void cleanup() { }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("word"));
    }

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}
}
package com.github.diegopachco.sandbox.java.storm.localcluster;

import java.util.HashMap;
import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;

public class WordCounterBolt implements IRichBolt {
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
    private String name;
    private Map<String, Integer> counters;
    private OutputCollector collector;

    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        counters = new HashMap<String, Integer>();
        collector = outputCollector;
        name = topologyContext.getThisComponentId();
        id = topologyContext.getThisTaskId();
    }

    @Override
    public void execute(Tuple tuple) {
        System.out.println("**** word counting **** " + tuple.getString(0));
        String str = tuple.getString(0);
        if (!counters.containsKey(str)) {
            counters.put(str, 1);
        } else {
            Integer c = counters.get(str) + 1;
            counters.put(str, c);
        }
        collector.ack(tuple);
    }

    @Override
    public void cleanup() {
        System.out.println(" *** Final result *** " );
        for (Map.Entry<String, Integer> entry : counters.entrySet()) {
            System.out.println(entry.getKey() + " -- :  -- " + entry.getValue());
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
    	
    }

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}

}
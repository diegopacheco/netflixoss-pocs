package com.github.diegopachco.sandbox.java.storm.localcluster;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class WordReaderBolt implements IRichSpout {
	private static final long serialVersionUID = 1L;

	private SpoutOutputCollector collector;
    private FileReader fileReader;
    private TopologyContext context;
    private boolean isCompleted = false;

    public boolean isDistributed() {
        return false;
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("line"));
    }

    @Override
    public void open(Map conf, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        context = topologyContext;
        try {
            fileReader = new FileReader(conf.get("wordsFile").toString());
        } catch (Exception e) {
            throw new RuntimeException("Error when reading file " + conf.get("wordsFile"), e);
        }

        collector = spoutOutputCollector;
    }

    @Override
    public void close() {
    }

    @Override
    public void nextTuple() {
        if (isCompleted) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException("Error when fetching next tuple " + e.getMessage());
            }
        }

        String str;
        BufferedReader reader = new BufferedReader(fileReader);
        try {
            while ((str = reader.readLine()) != null) {
                collector.emit(new Values(str), str);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error when reading tuple", e);
        } finally {
            isCompleted = true;
        }

    }

    @Override
    public void ack(Object o) {
        System.out.println("oK: " + o);
    }

    @Override
    public void fail(Object o) {
        System.out.println("Fail: " + o);
    }

	@Override
	public void activate() {
	}

	@Override
	public void deactivate() {
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}

}
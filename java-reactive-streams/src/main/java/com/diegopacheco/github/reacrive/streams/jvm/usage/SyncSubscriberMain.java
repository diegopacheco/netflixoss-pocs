package com.diegopacheco.github.reacrive.streams.jvm.usage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.reactivestreams.Publisher;
import org.reactivestreams.example.unicast.NumberIterablePublisher;
import org.reactivestreams.example.unicast.SyncSubscriber;

public class SyncSubscriberMain {
	public static void main(String[] args) {
		
		  ExecutorService e = Executors.newFixedThreadPool(4);
		  
		  SyncSubscriber<Integer> sub = new SyncSubscriber<Integer>(){
		      private long acc = 0;
		      
		      @Override protected boolean foreach(final Integer element) {
		        acc += element;
		        return true;
		      }
		      
		      @Override public void onComplete() {
		        System.out.println("Accumulated: " + acc);
		      }
		    };

		   Publisher<Integer> pub = new NumberIterablePublisher(0,10,e);
		   pub.subscribe(sub);
		   
	}
}

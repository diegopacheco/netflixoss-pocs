package com.github.diegopacheco.java.sanbox.cassandradatastax.main.stmt.rx;

import java.util.UUID;

import rx.Subscriber;
import rx.subjects.PublishSubject;

import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.QueryBuilder;

public class SessionBackpressureRunner {
	
	private static final PublishSubject<String> streamSource = PublishSubject.create();

	public SessionBackpressureRunner(final Session session) {
			streamSource
				  .buffer(1)
				  .subscribe(new Subscriber<Object>() {
						public void onCompleted() {
							System.out.println("DONE ALL FINITO!");
						}
						public void onError(Throwable e) {
							System.out.println("Eror:" + e);
						}
						public void onNext(Object c) {
							session.execute(QueryBuilder.insertInto("datastax_mapper_test","users")
							        .value("user_id", UUID.randomUUID())
							        .value("name", "Diego")
							        .value("email", "diegoSQN@gmail.com")
							        .value("year","1984"));

							session.execute(
									QueryBuilder
									.select()
									.from("datastax_mapper_test","users"));
							
							System.out.println("DONE");
						}
					});
	}
	
	public void run(String v){
		streamSource.onNext(v);
	}
}

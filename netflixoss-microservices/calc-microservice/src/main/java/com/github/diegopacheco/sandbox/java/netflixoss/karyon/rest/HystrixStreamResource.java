package com.github.diegopacheco.sandbox.java.netflixoss.karyon.rest;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.config.DynamicIntProperty;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsPoller;

@Singleton
@Path("/metrics")
@Produces(MediaType.APPLICATION_JSON)
public class HystrixStreamResource {

	private static final Logger logger = LoggerFactory.getLogger(HystrixStreamResource.class);

	private static AtomicInteger concurrentConnections = new AtomicInteger(0);
	private static DynamicIntProperty maxConnections = DynamicPropertyFactory.getInstance().getIntProperty("hystrix.stream.maxConnections", 5);
	private static DynamicIntProperty metricListenerQueueSize = DynamicPropertyFactory.getInstance().getIntProperty("hystrix.stream.metricListenerQueueSize", 1000);

	@GET
	@Path("hystrix.stream")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHystrixStreamResource(@QueryParam("delay") Integer delay) {
		int numberConnections = concurrentConnections.incrementAndGet();

		if (numberConnections > maxConnections.get() || isDestroyed) {
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
		}

		if (delay == null)
			delay = Integer.valueOf(500);

		return Response.ok(new HystrixMetrics(delay)).header("Content-Type", "text/event-stream;charset=UTF-8")
				.header("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate").
				 header("Pragma", "no-cache")
				.build();
	}
	
	private static class MetricJsonListener implements HystrixMetricsPoller.MetricsAsJsonPollerListener {
		private LinkedBlockingQueue<String> jsonMetrics;

		public MetricJsonListener(int queueSize) {
			jsonMetrics = new LinkedBlockingQueue<String>(queueSize);
		}

		@Override
		public void handleJsonMetric(String json) {
			jsonMetrics.add(json);
		}

		public List<String> getJsonMetrics() {
			ArrayList<String> metrics = new ArrayList<String>();
			jsonMetrics.drainTo(metrics);
			return metrics;
		}
	}

	private static volatile boolean isDestroyed = false;

	public static void shutdown() {
		isDestroyed = true;
	}

	private class HystrixMetrics implements StreamingOutput {
		private final int delay;

		HystrixMetrics(int delay) {
			this.delay = Math.max(delay, 100);
		}

		@Override
		public void write(OutputStream output) throws IOException, WebApplicationException {
			HystrixMetricsPoller poller = null;

			final PrintWriter response = new PrintWriter(output);

			try {
				int queueSize = metricListenerQueueSize.get();

				MetricJsonListener jsonListener = new MetricJsonListener(queueSize);
				poller = new HystrixMetricsPoller(jsonListener, delay);

				poller.start();
				logger.debug("Starting poller");

				try {
					while (poller.isRunning() && !isDestroyed) {
						List<String> jsonMessages = jsonListener.getJsonMetrics();
						if (jsonMessages.isEmpty()) {
							response.println("ping: \n");
						} else {
							for (String json : jsonMessages) {
								response.println("data: " + json + "\n");
							}
						}
						if (isDestroyed) {
							break;
						}
						response.flush();
						if (response.checkError()) {
							throw new IOException("io error");
						}

						Thread.sleep(delay);
					}
				} catch (InterruptedException e) {
					poller.shutdown();
					logger.debug("InterruptedException. Will stop polling.");
					Thread.currentThread().interrupt();

				} catch (IOException e) {
					poller.shutdown();
					logger.debug("IOException while trying to write (generally caused by client disconnecting). Will stop polling.",e);
				} catch (Exception e) {
					poller.shutdown();
					logger.error("Failed to write Hystrix metrics. Will stop polling.", e);
				}
				logger.debug("Stopping Turbine stream to connection");

			} catch (Exception e) {
				logger.error("Error initializing servlet for metrics event stream.", e);
			} finally {
				concurrentConnections.decrementAndGet();
				if (poller != null) {
					poller.shutdown();
				}
			}
		}
	}
}

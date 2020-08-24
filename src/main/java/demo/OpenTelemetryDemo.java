/*
 * Copyright 2020 New Relic Corporation. All rights reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package demo;

import com.newrelic.telemetry.opentelemetry.export.NewRelicExporters;
import com.newrelic.telemetry.opentelemetry.export.NewRelicExporters.Configuration;
import io.opentelemetry.OpenTelemetry;
import io.opentelemetry.common.Labels;
import io.opentelemetry.context.Scope;
import io.opentelemetry.metrics.LongCounter;
import io.opentelemetry.metrics.LongValueRecorder;
import io.opentelemetry.metrics.LongValueRecorder.BoundLongValueRecorder;
import io.opentelemetry.metrics.Meter;
import io.opentelemetry.trace.Span;
import io.opentelemetry.trace.Span.Kind;
import io.opentelemetry.trace.Status;
import io.opentelemetry.trace.Tracer;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpenTelemetryDemo {
  private Logger logger = LoggerFactory.getLogger(OpenTelemetryDemo.class);

  LongCounter spanCounter ;
  Tracer tracer ;
  Meter meter ;
  Configuration configuration;
  LongValueRecorder spanTimer;
  BoundLongValueRecorder boundTimer;
  String apiKey = System.getenv("NR_INSIGHTS_INSERT_KEY");
  String applicationName = "OpenTelemetryDemo";
  
  public void OpenTelemetryDemo() 
  {
    
    this.applicationName = "OpenTelemetryDemo";
    return;

  }

  public void init() throws Exception
  {
    //String apiKey = System.getenv("INSIGHTS_INSERT_KEY");
    
    // logger = LoggerFactory.getLogger(OpenTelemetryDemo.class);
    logger.info("initialize the OpenTelemetry Exporter");
       
    // 1. The simplest way to configure the New Relic exporters is like this:
    configuration =
        new Configuration(apiKey, "best service ever")
            .enableAuditLogging()
            .collectionIntervalSeconds(10);

    NewRelicExporters.start(configuration);

    // Now, we've got the SDK configured and the exporters started.
    // Let's write some very simple instrumentation to demonstrate how it all works.

    // 2. Create an OpenTelemetry `Tracer` and a `Meter` and use them for some manual
    // instrumentation.

    tracer = OpenTelemetry.getTracerProvider().get(applicationName, "1.0");
    meter = OpenTelemetry.getMeterProvider().get(applicationName, "1.0");

    // 3. Here is an example of a counter
    spanCounter =
        meter
            .longCounterBuilder("spanCounter")
            .setUnit("one")
            .setDescription("Counting all the spans")
            .build();

    // 4. Here's an example of a measure.
    spanTimer =
        meter
            .longValueRecorderBuilder("spanTimer")
            .setUnit("ms")
            .setDescription("How long the spans take")
            .build();

    // 5. Optionally, you can pre-bind a set of labels, rather than passing them in every time.
    boundTimer = spanTimer.bind(Labels.of("spanName", "testSpan"));

    // 6. use these to instrument some work
    // doSomeSimulatedWork(tracer, spanCounter, boundTimer);

    // clean up so the JVM can exit. Note: these will flush any data to the exporter
    // before they exit.
    // NewRelicExporters.shutdown();

    
  }

  public void doRequest(String requestName) throws Exception
  {
    logger.info("getting request : " + requestName);
  
    doSomeSimulatedWork();
  }

  //private static void doSomeSimulatedWork(
  //Tracer tracer, LongCounter spanCounter, BoundLongValueRecorder boundTimer)
  public void doSomeSimulatedWork() throws InterruptedException 
  {
    Random random = new Random();
    for (int i = 0; i < 10; i++) {
      long startTime = System.currentTimeMillis();
      Span span =
          tracer.spanBuilder("testSpan").setSpanKind(Kind.INTERNAL).setNoParent().startSpan();
      try (Scope ignored = tracer.withSpan(span)) {
        boolean markAsError = random.nextBoolean();
        if (markAsError) {
          span.setStatus(Status.INTERNAL.withDescription("internalError"));
        }
        spanCounter.add(1, Labels.of("spanName", "testSpan", "isItAnError", "" + markAsError));
        // do some work
        Thread.sleep(random.nextInt(1000));
        span.end();
        boundTimer.record(System.currentTimeMillis() - startTime);
      }
    }
  }
}
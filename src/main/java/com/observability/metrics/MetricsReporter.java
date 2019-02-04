package com.observability.metrics;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import org.coursera.metrics.datadog.DatadogReporter;
import org.coursera.metrics.datadog.transport.HttpTransport;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MetricsReporter {
  private MetricRegistry metricRegistry = new MetricRegistry();
  private DatadogReporter reporter;
  private final Long timePeriod = 5000L;
  private final String apiKey = "";
  List<String> tags = new ArrayList<String>();

  private void addTags() {
    tags.add("severity:info");
  }

  public MetricsReporter() {
    EnumSet<DatadogReporter.Expansion> expansions = DatadogReporter.Expansion.ALL;
    HttpTransport httpTransport = new HttpTransport.Builder().withApiKey(apiKey).build();
    addTags();

    reporter = DatadogReporter.forRegistry(metricRegistry)
            .withTags(tags)
            .withTransport(httpTransport)
            .withExpansions(expansions)
            .build();

    reporter.start(timePeriod, TimeUnit.MILLISECONDS);
  }

  public void testCounter() {
    Counter includedCounter = metricRegistry.counter(MetricRegistry.name(this.getClass(), "testCount"));

    for (int i = 0; i < 1000; i++) {
      includedCounter.inc();
    }
  }

  public static final void main(String[] args) {
    MetricsReporter reporter = new MetricsReporter();
    reporter.testCounter();

    while(true) {
      // Keep Running
    }
  }
}

package com.complone.prom;

import org.apache.flink.metrics.reporter.InterceptInstantiationViaReflection;
import org.apache.flink.metrics.reporter.MetricReporterFactory;

import java.util.Properties;


@InterceptInstantiationViaReflection(reporterClassName = "com.complone.prom.PrometheusConsulReporter")
public class PrometheusConsulReporterFactory implements MetricReporterFactory {

    @Override
    public PrometheusConsulReporter createMetricReporter(Properties properties) {
        return new PrometheusConsulReporter();
    }
}

package com.complone.flink.metirc;

import com.codahale.metrics.SlidingWindowReservoir;
import org.apache.flink.dropwizard.metrics.DropwizardHistogramWrapper;
import org.apache.flink.metrics.Histogram;
import org.apache.flink.metrics.MetricGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LatencyMetricTools {
    private static final Logger logger = LoggerFactory.getLogger(LatencyMetricTools.class);

    MetricGroup metricGroup;

    public static final String LATENCY = "latency_ms";

    protected transient Histogram latency;

    private  Integer slidingWindow = 1000;

    public LatencyMetricTools (MetricGroup metricGroup,String user_defined_group_name) {
        this.metricGroup = metricGroup;
        com.codahale.metrics.Histogram endToendDropwizardHistogram  =  new com.codahale.metrics.Histogram(new SlidingWindowReservoir(slidingWindow));
        latency = metricGroup.addGroup(user_defined_group_name).histogram(LATENCY,new DropwizardHistogramWrapper(endToendDropwizardHistogram));
    }

    public void latency(long delay){
        if (delay >0) {
            latency.update(delay);
        }else {
            latency.update(-1L);
        }
    }

    public  void setSlidingWindow(Integer slidingWindow) {
        this.slidingWindow = slidingWindow;
    }

}

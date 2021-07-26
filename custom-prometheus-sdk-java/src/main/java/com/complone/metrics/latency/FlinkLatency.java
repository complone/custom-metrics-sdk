package com.complone.metrics.latency;

import com.codahale.metrics.SlidingWindowReservoir;
import org.apache.flink.dropwizard.metrics.DropwizardHistogramWrapper;
import org.apache.flink.metrics.MetricGroup;


public class FlinkLatency extends Latency {
    protected org.apache.flink.metrics.Histogram latency;

    public FlinkLatency(MetricGroup metricGroup, String userGroup,int slidingWindow){
        com.codahale.metrics.Histogram endToendDropwizardHistogram  =  new com.codahale.metrics.Histogram(new SlidingWindowReservoir(slidingWindow));
        latency = metricGroup.addGroup(userGroup).histogram(LATENCY,new DropwizardHistogramWrapper(endToendDropwizardHistogram));

    }

    @Override
    public void latency(long delay) {
        if (delay >0) {
            latency.update(delay);
        }else {
            latency.update(-1L);
        }
    }


}

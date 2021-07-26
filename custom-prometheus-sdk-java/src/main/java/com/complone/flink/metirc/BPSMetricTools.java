package com.complone.flink.metirc;

import org.apache.flink.metrics.Counter;
import org.apache.flink.metrics.Meter;
import org.apache.flink.metrics.MeterView;
import org.apache.flink.metrics.MetricGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BPSMetricTools {
    private static final Logger logger = LoggerFactory.getLogger(BPSMetricTools.class);

    MetricGroup metricGroup;

    public static final String NUM_BYTES_COUNTER = "numBytesCounter";
    public static final String NUM_BYTES_PERSECOND = "numBytesPerSecond_bps";

    protected transient Counter numBytesCounter;
    protected transient Meter numBytesPerSecond;

    private  Integer metricTimeSpanInSeconds = 10;

    public BPSMetricTools (MetricGroup metricGroup,String user_defined_group_name) {
        this.metricGroup = metricGroup;
        numBytesCounter = metricGroup.addGroup(user_defined_group_name).counter(NUM_BYTES_COUNTER);
        numBytesPerSecond = metricGroup.addGroup(user_defined_group_name).meter(NUM_BYTES_PERSECOND,new MeterView(numBytesCounter,metricTimeSpanInSeconds));
    }

    public void numBytesCounterInc(byte[] value){
        try {
            numBytesCounter.inc(value.length);
        } catch (Exception e){
            logger.error("numBytesCounter inc err:",e);
        }
    }

    public  void setMetricTimeSpanInSeconds(Integer metricTimeSpanInSeconds) {
        this.metricTimeSpanInSeconds = metricTimeSpanInSeconds;
    }

}

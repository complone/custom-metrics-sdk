package com.complone.flink.metirc;

import com.codahale.metrics.SlidingWindowReservoir;
import org.apache.flink.dropwizard.metrics.DropwizardHistogramWrapper;
import org.apache.flink.metrics.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 提供基本监控指标包括：qps、bps、latency
 */
public class BasicMetricsTools {
    private static final Logger logger = LoggerFactory.getLogger(BasicMetricsTools.class);

    MetricGroup metricGroup;

    public static final String NUM_RECORDS_COUNTER = "numRecordsCounter";
    public static final String NUM_RECORDS_RATE_PERSECOND = "numRecordsRatePerSecond_qps";

    public static final String NUM_BYTES_COUNTER = "numBytesCounter";
    public static final String NUM_BYTES_PERSECOND = "numBytesPerSecond_bps";

    public static final String LATENCY = "latency_ms";

    protected transient Counter numRecordCounter;
    protected transient Meter numRecordPerSecond;

    protected transient Counter numBytesCounter;
    protected transient Meter numBytesPerSecond;

    protected transient Histogram latency;

    private  Integer metricTimeSpanInSeconds = 10;
    private  Integer slidingWindow = 1000;

  /*  public PrometheusMetricsTools(MetricGroup metricGroup){
        this.metricGroup = metricGroup;
        numRecordCounter = metricGroup.counter(NUM_RECORDS_COUNTER);
        numRecordPerSecond = metricGroup.meter(NUM_RECORDS_RATE_PERSECOND,new MeterView(numRecordCounter,metricTimeSpanInSeconds));

        numBytesCounter = metricGroup.counter(NUM_BYTES_COUNTER);
        numBytesPerSecond = metricGroup.meter(NUM_BYTES_PERSECOND,new MeterView(numBytesCounter,metricTimeSpanInSeconds));

        com.codahale.metrics.Histogram endToendDropwizardHistogram  =  new com.codahale.metrics.Histogram(new SlidingWindowReservoir(slidingWindow));
        endToendLatency = metricGroup.histogram(END_TO_END_LATENCY,new DropwizardHistogramWrapper(endToendDropwizardHistogram));
    }*/

    public BasicMetricsTools(MetricGroup metricGroup, String user_defined_group_name ){
        this.metricGroup = metricGroup;
        numRecordCounter = metricGroup.addGroup(user_defined_group_name).counter(NUM_RECORDS_COUNTER);
        numRecordPerSecond = metricGroup.addGroup(user_defined_group_name).meter(NUM_RECORDS_RATE_PERSECOND,new MeterView(numRecordCounter,metricTimeSpanInSeconds));

        numBytesCounter = metricGroup.addGroup(user_defined_group_name).counter(NUM_BYTES_COUNTER);
        numBytesPerSecond = metricGroup.addGroup(user_defined_group_name).meter(NUM_BYTES_PERSECOND,new MeterView(numBytesCounter,metricTimeSpanInSeconds));

        com.codahale.metrics.Histogram endToendDropwizardHistogram  =  new com.codahale.metrics.Histogram(new SlidingWindowReservoir(slidingWindow));
        latency = metricGroup.addGroup(user_defined_group_name).histogram(LATENCY,new DropwizardHistogramWrapper(endToendDropwizardHistogram));
    }

    public void numRecordsCounterInc(){
        try {
            numRecordCounter.inc();
        } catch (Exception e){
            logger.error("numRecordCounter inc err:",e);
        }
    }

    public void numBytesCounterInc(byte[] value){
        try {
            numBytesCounter.inc(value.length);
        } catch (Exception e){
            logger.error("numBytesCounter inc err:",e);
        }
    }

    public void latency(long delay){
        if (delay >0) {
            latency.update(delay);
        }else {
            latency.update(-1L);
        }
    }

    public  void setMetricTimeSpanInSeconds(Integer metricTimeSpanInSeconds) {
        this.metricTimeSpanInSeconds = metricTimeSpanInSeconds;
    }

    public  void setSlidingWindow(Integer slidingWindow) {
        this.slidingWindow = slidingWindow;
    }

}

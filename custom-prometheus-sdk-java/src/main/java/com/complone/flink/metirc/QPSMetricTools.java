package com.complone.flink.metirc;

import org.apache.flink.metrics.Counter;
import org.apache.flink.metrics.Meter;
import org.apache.flink.metrics.MeterView;
import org.apache.flink.metrics.MetricGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QPSMetricTools {
    private static final Logger logger = LoggerFactory.getLogger(QPSMetricTools.class);

    MetricGroup metricGroup;

    public static final String NUM_RECORDS_COUNTER = "numRecordsCounter";
    public static final String NUM_RECORDS_RATE_PERSECOND = "numRecordsRatePerSecond_qps";

    protected transient Counter numRecordCounter;
    protected transient Meter numRecordPerSecond;

    private  Integer metricTimeSpanInSeconds = 10;

    public QPSMetricTools (MetricGroup metricGroup,String user_defined_group_name) {
        this.metricGroup = metricGroup;
        numRecordCounter = metricGroup.addGroup(user_defined_group_name).counter(NUM_RECORDS_COUNTER);
        numRecordPerSecond = metricGroup.addGroup(user_defined_group_name).meter(NUM_RECORDS_RATE_PERSECOND,new MeterView(numRecordCounter,metricTimeSpanInSeconds));
    }

    public void numRecordsCounterInc(){
        try {
            numRecordCounter.inc();
        } catch (Exception e){
            logger.error("numRecordCounter inc err:",e);
        }
    }

    public  void setMetricTimeSpanInSeconds(Integer metricTimeSpanInSeconds) {
        this.metricTimeSpanInSeconds = metricTimeSpanInSeconds;
    }
}


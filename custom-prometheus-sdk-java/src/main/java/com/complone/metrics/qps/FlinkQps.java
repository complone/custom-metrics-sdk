package com.complone.metrics.qps;

import org.apache.flink.metrics.MetricGroup;

public class FlinkQps extends Qps {
    protected org.apache.flink.metrics.Counter numOfRecords;
    public FlinkQps(MetricGroup metricGroup, String userGroup){
        numOfRecords = metricGroup.addGroup(userGroup).counter(NUM_RECORDS);
    }

    @Override
   public void inc(int var) {
        numOfRecords.inc(var);
    }
}

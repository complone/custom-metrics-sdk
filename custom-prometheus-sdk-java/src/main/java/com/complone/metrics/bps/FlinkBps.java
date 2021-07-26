package com.complone.metrics.bps;

import org.apache.flink.metrics.MetricGroup;

public class FlinkBps extends Bps {
    protected org.apache.flink.metrics.Counter numOfBytes;

    public FlinkBps(MetricGroup metricGroup, String userGroup){
        numOfBytes = metricGroup.addGroup(userGroup).counter(NUM_BYTES);
    }

    @Override
    public void inc(int var) {
        numOfBytes.inc(var);
    }
}

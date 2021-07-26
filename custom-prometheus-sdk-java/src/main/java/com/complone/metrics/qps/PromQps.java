package com.complone.metrics.qps;

import io.prometheus.client.Counter;

public class PromQps extends Qps {
    protected Counter numOfRecords ;

    public PromQps(){
        numOfRecords = Counter.build().name("").register();
    }

    @Override
    public void inc(int var) {
        numOfRecords.inc(var);
    }
}

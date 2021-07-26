package com.complone.metrics.bps;

import io.prometheus.client.Counter;

public class PromBps extends Bps {
    protected Counter numOfBytes ;

    public PromBps(){
        numOfBytes = Counter.build().name("").register();
    }

    @Override
    public void inc(int var) {
        numOfBytes.inc(var);
    }
}

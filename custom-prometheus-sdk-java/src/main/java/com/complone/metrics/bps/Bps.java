package com.complone.metrics.bps;

import com.complone.env.Collectors;
import com.complone.env.FlinkCollectors;
import com.complone.env.PrometheusCollectors;

/**
 * 利用prom sql 计算bps : sum(rate(xxx_numOfBytes{job_name="xxx"}[1m]))
 */
public abstract class Bps {
    public static String NUM_BYTES = "numOfBytes";
    public static Bps bps;
    public static Bps init(Collectors collectors){
        if (collectors instanceof PrometheusCollectors){
            bps = new PromBps();
        }else if (collectors instanceof FlinkCollectors){
            bps = new FlinkBps(((FlinkCollectors) collectors).getMetricGroup(),((FlinkCollectors) collectors).getUserGroup());

        }
        return bps;
    }

    public abstract void inc(int var);
}

package com.complone.metrics.latency;

import com.complone.env.Collectors;
import com.complone.env.FlinkCollectors;
import com.complone.env.PrometheusCollectors;

/**
 * 延迟分位值、最大值、最小值、中位值：max(xxx_latency_ms{job_name=xxx}) by(quantile)
 */
public abstract class Latency {
    public static String LATENCY = "latency_ms";
    public static Latency latency;
    public static Integer DEFAULT_WINDOW = 1000;
    public static Latency init(Collectors collectors,int window){
        DEFAULT_WINDOW = window;
        if (collectors instanceof PrometheusCollectors){
            latency = new PromLatency();
        }else if (collectors instanceof FlinkCollectors){
            latency = new FlinkLatency(((FlinkCollectors) collectors).getMetricGroup(),((FlinkCollectors) collectors).getUserGroup(),DEFAULT_WINDOW);

        }
        return latency;
    }

    public abstract void latency(long value);
}

package com.complone.metrics.qps;

import com.complone.env.Collectors;
import com.complone.env.FlinkCollectors;
import com.complone.env.PrometheusCollectors;

/**
 * 利用prom sql 计算qps : sum(rate(xxx_numOfRecords{job_name="xxx"}[1m]))
 */
public abstract  class Qps {
    public static  String NUM_RECORDS = "numOfRecords";
    public  static Qps qps;
    public static Qps init(Collectors collectors){
        if (collectors instanceof PrometheusCollectors){
            qps = new PromQps();
        }else if (collectors instanceof FlinkCollectors){
            qps = new FlinkQps(((FlinkCollectors) collectors).getMetricGroup(),((FlinkCollectors) collectors).getUserGroup());

        }
        return qps;
    };

     public  abstract void inc(int var);
}


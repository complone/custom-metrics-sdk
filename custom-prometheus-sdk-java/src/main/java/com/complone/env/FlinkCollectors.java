package com.complone.env;

import org.apache.flink.metrics.MetricGroup;

public class FlinkCollectors extends Collectors {
    private  MetricGroup metricGroup;
    private  String userGroup;
    private  String type;

    public FlinkCollectors(MetricGroup mg, String ug){
        this.metricGroup =  mg;
        this.userGroup = ug;
        this.type = Env.FLINK.toString();
    }

    public void setMetricGroup(MetricGroup metricGroup) {
        this.metricGroup = metricGroup;
    }

    public void setUserGroup(String userGroup) {
        this.userGroup = userGroup;
    }

    public MetricGroup getMetricGroup() {
        return metricGroup;
    }

    public String getUserGroup() {
        return userGroup;
    }
}

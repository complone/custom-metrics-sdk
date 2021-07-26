package com.complone.env;

public class PrometheusCollectors extends Collectors {
    private String type;

    public PrometheusCollectors(){
        this.type = Env.PROMETHEUS.toString();
    }
}

package com.itesm.panoptimize.dto.connect;

public class Collection {
    private Metric metric;
    private Double value;

    public Metric getMetric() { return metric; }
    public void setMetric(Metric value) { this.metric = value; }

    public Double getValue() { return value; }
    public void setValue(Double value) { this.value = value; }
}

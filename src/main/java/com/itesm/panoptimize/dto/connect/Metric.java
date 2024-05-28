package com.itesm.panoptimize.dto.connect;

import java.util.List;

public class Metric {
    private List<MetricFilter> metricFilters;
    private String name;
    private List<Threshold> threshold;

    public List<MetricFilter> getMetricFilters() { return metricFilters; }
    public void setMetricFilters(List<MetricFilter> value) { this.metricFilters = value; }

    public String getName() { return name; }
    public void setName(String value) { this.name = value; }

    public List<Threshold> getThreshold() { return threshold; }
    public void setThreshold(List<Threshold> value) { this.threshold = value; }
}
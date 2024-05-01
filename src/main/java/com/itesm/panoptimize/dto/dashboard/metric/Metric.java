package com.itesm.panoptimize.dto.dashboard.metric;

public class Metric {
    private MetricFilter[] metricFilters;
    private String name;
    private Threshold[] threshold;

    public MetricFilter[] getMetricFilters() { return metricFilters; }
    public void setMetricFilters(MetricFilter[] value) { this.metricFilters = value; }

    public String getName() { return name; }
    public void setName(String value) { this.name = value; }

    public Threshold[] getThreshold() { return threshold; }
    public void setThreshold(Threshold[] value) { this.threshold = value; }
}


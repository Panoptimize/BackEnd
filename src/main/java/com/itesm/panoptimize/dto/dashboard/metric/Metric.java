package com.itesm.panoptimize.dto.dashboard.metric;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Metric {
    @JsonProperty("metric_filters")
    private List<MetricFilter> metricFilters;
    @JsonProperty("name")
    private String name;
    @JsonProperty("threshold")
    private List<Threshold> threshold;

    public List<MetricFilter> getMetricFilters() { return metricFilters; }
    public void setMetricFilters(List<MetricFilter> value) { this.metricFilters = value; }

    public String getName() { return name; }
    public void setName(String value) { this.name = value; }

    public List<Threshold> getThreshold() { return threshold; }
    public void setThreshold(List<Threshold> value) { this.threshold = value; }
}


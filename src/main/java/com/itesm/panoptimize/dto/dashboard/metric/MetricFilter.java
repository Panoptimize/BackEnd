package com.itesm.panoptimize.dto.dashboard.metric;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class MetricFilter {
    @JsonProperty("metric_filter_key")
    private String metricFilterKey;
    @JsonProperty("metric_filter_values")
    private List<String> metricFilterValues;
    @JsonProperty("negate")
    private boolean negate;

    public String getMetricFilterKey() { return metricFilterKey; }
    public void setMetricFilterKey(String value) { this.metricFilterKey = value; }

    public List<String> getMetricFilterValues() { return metricFilterValues; }
    public void setMetricFilterValues(List<String> value) { this.metricFilterValues = value; }

    public boolean getNegate() { return negate; }
    public void setNegate(boolean value) { this.negate = value; }
}

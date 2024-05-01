package com.itesm.panoptimize.dto.dashboard.metric;

public class MetricFilter {
    private String metricFilterKey;
    private String[] metricFilterValues;
    private boolean negate;

    public String getMetricFilterKey() { return metricFilterKey; }
    public void setMetricFilterKey(String value) { this.metricFilterKey = value; }

    public String[] getMetricFilterValues() { return metricFilterValues; }
    public void setMetricFilterValues(String[] value) { this.metricFilterValues = value; }

    public boolean getNegate() { return negate; }
    public void setNegate(boolean value) { this.negate = value; }
}

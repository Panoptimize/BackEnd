package com.itesm.panoptimize.dto.connect;

import java.util.List;

public class MetricFilter {
    private String metricFilterKey;
    private List<String> metricFilterValues;
    private boolean negate;

    public String getMetricFilterKey() { return metricFilterKey; }
    public void setMetricFilterKey(String value) { this.metricFilterKey = value; }

    public List<String> getMetricFilterValues() { return metricFilterValues; }
    public void setMetricFilterValues(List<String> value) { this.metricFilterValues = value; }

    public boolean getNegate() { return negate; }
    public void setNegate(boolean value) { this.negate = value; }
}

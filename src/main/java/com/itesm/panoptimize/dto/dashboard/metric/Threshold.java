package com.itesm.panoptimize.dto.dashboard.metric;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Threshold {
    @JsonProperty("comparison")
    private String comparison;
    @JsonProperty("threshold_value")
    private long thresholdValue;

    public String getComparison() { return comparison; }
    public void setComparison(String value) { this.comparison = value; }

    public long getThresholdValue() { return thresholdValue; }
    public void setThresholdValue(long value) { this.thresholdValue = value; }

}
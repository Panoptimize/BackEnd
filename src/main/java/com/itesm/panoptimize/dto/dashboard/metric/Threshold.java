package com.itesm.panoptimize.dto.dashboard.metric;

public class Threshold {
    private String comparison;
    private long thresholdValue;

    public String getComparison() { return comparison; }
    public void setComparison(String value) { this.comparison = value; }

    public long getThresholdValue() { return thresholdValue; }
    public void setThresholdValue(long value) { this.thresholdValue = value; }
}

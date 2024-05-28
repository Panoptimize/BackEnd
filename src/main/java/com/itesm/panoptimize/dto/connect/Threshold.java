package com.itesm.panoptimize.dto.connect;

public class Threshold {
    private String comparison;
    private Double thresholdValue;

    public String getComparison() { return comparison; }
    public void setComparison(String value) { this.comparison = value; }

    public Double getThresholdValue() { return thresholdValue; }
    public void setThresholdValue(Double value) { this.thresholdValue = value; }
}

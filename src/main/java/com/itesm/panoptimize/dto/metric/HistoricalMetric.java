package com.itesm.panoptimize.dto.metric;

public class HistoricalMetric {
    private String name;
    private String statistic;
    private Threshold threshold;
    private String unit;

    public String getName() { return name; }
    public void setName(String value) { this.name = value; }

    public String getStatistic() { return statistic; }
    public void setStatistic(String value) { this.statistic = value; }

    public Threshold getThreshold() { return threshold; }
    public void setThreshold(Threshold value) { this.threshold = value; }

    public String getUnit() { return unit; }
    public void setUnit(String value) { this.unit = value; }
}
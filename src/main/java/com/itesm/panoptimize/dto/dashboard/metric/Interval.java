package com.itesm.panoptimize.dto.dashboard.metric;

public class Interval {
    private String intervalPeriod;
    private String timeZone;

    public String getIntervalPeriod() { return intervalPeriod; }
    public void setIntervalPeriod(String value) { this.intervalPeriod = value; }

    public String getTimeZone() { return timeZone; }
    public void setTimeZone(String value) { this.timeZone = value; }
}

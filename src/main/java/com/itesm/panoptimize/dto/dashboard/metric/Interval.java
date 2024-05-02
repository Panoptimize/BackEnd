package com.itesm.panoptimize.dto.dashboard.metric;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Interval {
    @JsonProperty("interval_period")
    private String intervalPeriod;
    @JsonProperty("time_zone")
    private String timeZone;

    public String getIntervalPeriod() { return intervalPeriod; }
    public void setIntervalPeriod(String value) { this.intervalPeriod = value; }

    public String getTimeZone() { return timeZone; }
    public void setTimeZone(String value) { this.timeZone = value; }
}

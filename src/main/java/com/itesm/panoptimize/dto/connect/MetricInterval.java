package com.itesm.panoptimize.dto.connect;

import software.amazon.awssdk.services.connect.model.IntervalPeriod;

import java.time.Instant;

public class MetricInterval {
    private Instant endTime;
    private IntervalPeriod interval;
    private Instant startTime;

    public Instant getEndTime() { return endTime; }
    public void setEndTime(Instant value) { this.endTime = value; }

    public IntervalPeriod getInterval() { return interval; }
    public void setInterval(IntervalPeriod value) { this.interval = value; }

    public Instant getStartTime() { return startTime; }
    public void setStartTime(Instant value) { this.startTime = value; }
}

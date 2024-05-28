package com.itesm.panoptimize.dto.dashboard;

import java.time.Instant;
import java.util.List;

public class ActivityDTO {
    private Double value;
    private Instant startTime;

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }
}

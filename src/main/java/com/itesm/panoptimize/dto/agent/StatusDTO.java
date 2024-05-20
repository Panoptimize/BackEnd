package com.itesm.panoptimize.dto.agent;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Data Transfer Object (DTO) for representing the status metrics results.
 * This class is used to deserialize JSON responses that contain metrics results
 * structured in nested collections.
 */
public class StatusDTO {
    private final String metricName;
    private final Double metricValue;

    public StatusDTO(String metricName, Double metricValue) {
        this.metricName = metricName;
        this.metricValue = metricValue;
    }

    public String getMetricName() {
        return metricName;
    }

    public Double getMetricValue() {
        return metricValue;
    }
}
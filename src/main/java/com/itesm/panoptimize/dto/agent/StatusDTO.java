package com.itesm.panoptimize.dto.agent;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Data Transfer Object (DTO) for representing the status metrics results.
 * This class is used to deserialize JSON responses that contain metrics results
 * structured in nested collections.
 */
public class StatusDTO {
    @JsonProperty("MetricResults")
    private List<MetricResult> metricResults;

    /**
     * Represents a single metric result, which contains multiple collections of metrics.
     */
    public static class MetricResult {
        @JsonProperty("Collections")
        private List<Collection> collections;

    }

    /**
     * Represents a collection of metrics, encapsulating individual metrics.
     */
    public static class Collection {
        @JsonProperty("Metric")
        private Metric metric;

    }

    /**
     * Represents an individual metric, detailing the metric's name and its value.
     */
    public static class Metric {
        @JsonProperty("Name")
        private String name;

        @JsonProperty("Value")
        private double value;

    }


}
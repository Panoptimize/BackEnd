package com.itesm.panoptimize.dto.agent;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class StatusDTO {
    @JsonProperty("ApproximateTotalCount")
    private int approximateTotalCount;

    @JsonProperty("DataSnapshotTime")
    private String dataSnapshotTime;

    @JsonProperty("MetricResults")
    private List<MetricResult> metricResults;

    @JsonProperty("NextToken")
    private String nextToken;

    // getters and setters

    public static class MetricResult {
        @JsonProperty("Collections")
        private List<Collection> collections;

        @JsonProperty("Dimensions")
        private Dimensions dimensions;

        // getters and setters
    }

    public static class Collection {
        @JsonProperty("Metric")
        private Metric metric;

        @JsonProperty("Value")
        private double value;

        // getters and setters
    }

    public static class Metric {
        @JsonProperty("Name")
        private String name;

        @JsonProperty("Unit")
        private String unit;

        @JsonProperty("Value")
        private double value;

        // getters and setters
    }

    public static class Dimensions {
        @JsonProperty("Channel")
        private String channel;

        @JsonProperty("Queue")
        private Queue queue;

        @JsonProperty("RoutingProfile")
        private RoutingProfile routingProfile;

        @JsonProperty("RoutingStepExpression")
        private String routingStepExpression;

        // getters and setters
    }

    public static class Queue {
        @JsonProperty("Arn")
        private String arn;

        @JsonProperty("Id")
        private String id;

        // getters and setters
    }

    public static class RoutingProfile {
        @JsonProperty("Arn")
        private String arn;

        @JsonProperty("Id")
        private String id;

        // getters and setters
    }
}
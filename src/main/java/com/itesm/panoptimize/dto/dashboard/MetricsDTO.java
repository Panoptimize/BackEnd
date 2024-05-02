package com.itesm.panoptimize.dto.dashboard;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
public class MetricsDTO {
    @JsonProperty("metric_results")
    private List<MetricResult> metricResults;
    @JsonProperty("next_token")
    private String nextToken;

    public List<MetricResult> getMetricResults() {
        return metricResults;
    }

    public String getNextToken() {
        return nextToken;
    }

    public static class MetricResult {
        @JsonProperty("collections")
        private List<Collection> collections;

        public List<Collection> getCollections() {
            return collections;
        }

        public static class Collection {
            @JsonProperty("metric")
            private Metric metric;

            @JsonProperty("value")
            private double value;

            public Metric getMetric() {
                return metric;
            }

            public double getValue() {
                return value;
            }

            public static class Metric {
                @JsonProperty("name")
                private MetricName name;

                @JsonProperty("threshold")
                private Threshold metricThreshold;

                public MetricName getName() {
                    return name;
                }

                public static class MetricName {

                    @JsonProperty("name")
                    private String name;

                    @JsonProperty("threshold")
                    private List<Threshold> thresholds;

                    public String getName() {
                        return name;
                    }

                    public static class Threshold {
                        @JsonProperty("comparison")
                        private String comparison;

                        @JsonProperty("threshold_value")
                        private int thresholdValue;
                    }
                }

                public static class Threshold {
                    @JsonProperty("comparison")
                    private String comparison;

                    @JsonProperty("threshold_value")
                    private int thresholdValue;
                }
            }
        }

    }

}

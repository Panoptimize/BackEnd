package com.itesm.panoptimize.dto.dashboard;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
public class MetricsDTO {
    @JsonProperty("metric_results")
    private List<MetricResult> metricResults;


    public static class MetricResult {
        @JsonProperty("collections")
        private List<Collection> collections;


        public static class Collection {
            @JsonProperty("metric")
            private Metric metric;

            @JsonProperty("value")
            private int value;

            public static class Metric {
                @JsonProperty("name")
                private MetricName name;

                @JsonProperty("threshold")
                private Threshold metricThreshold;

                public static class MetricName {

                    @JsonProperty("name")
                    private String name;

                    @JsonProperty("threshold")
                    private List<Threshold> thresholds;


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

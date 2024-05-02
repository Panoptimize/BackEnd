package com.itesm.panoptimize.dto.contact;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CollectionDTO {
    @JsonProperty("Metric")
    private MetricDTO metric;

    @JsonProperty("Value")
    private int value;

    // Getters y Setters
    public MetricDTO getMetric() {
        return metric;
    }

    public void setMetric(MetricDTO metric) {
        this.metric = metric;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

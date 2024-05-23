package com.itesm.panoptimize.dto.connect;

import software.amazon.awssdk.services.connect.model.Dimensions;

import java.util.List;
import java.util.Map;

public class MetricResult {
    private List<Collection> collections;
    private Map<String, String> dimensions;
    private MetricInterval metricInterval;

    public List<Collection> getCollections() { return collections; }
    public void setCollections(List<Collection> value) { this.collections = value; }

    public Map<String, String> getDimensions() { return dimensions; }
    public void setDimensions(Map<String, String> value) { this.dimensions = value; }

    public MetricInterval getMetricInterval() { return metricInterval; }
    public void setMetricInterval(MetricInterval value) { this.metricInterval = value; }
}

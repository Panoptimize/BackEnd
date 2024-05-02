package com.itesm.panoptimize.dto.contact;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class MetricResultsDTO {
    @JsonProperty("MetricResults")
    private List<MetricResultDTO> metricResults;

    @JsonProperty("NextToken")
    private String nextToken;

    // Getters y Setters
    public List<MetricResultDTO> getMetricResults() {
        return metricResults;
    }

    public void setMetricResults(List<MetricResultDTO> metricResults) {
        this.metricResults = metricResults;
    }

    public String getNextToken() {
        return nextToken;
    }

    public void setNextToken(String nextToken) {
        this.nextToken = nextToken;
    }
}

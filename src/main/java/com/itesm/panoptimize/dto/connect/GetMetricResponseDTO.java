package com.itesm.panoptimize.dto.connect;

import java.util.List;

@Deprecated
public class GetMetricResponseDTO {
    private List<MetricResult> metricResults;
    private String nextToken;

    public List<MetricResult> getMetricResults() { return metricResults; }
    public void setMetricResults(List<MetricResult> value) { this.metricResults = value; }

    public String getNextToken() { return nextToken; }
    public void setNextToken(String value) { this.nextToken = value; }
}

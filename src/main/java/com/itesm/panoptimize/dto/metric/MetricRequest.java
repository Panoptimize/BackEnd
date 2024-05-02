package com.itesm.panoptimize.dto.metric;

import java.util.List;

public class MetricRequest {
    private long endTime;
    private Filters filters;
    private List<String> groupings;
    private List<HistoricalMetric> historicalMetrics;
    private long maxResults;
    private String nextToken;
    private long startTime;

    public long getEndTime() { return endTime; }
    public void setEndTime(long value) { this.endTime = value; }

    public Filters getFilters() { return filters; }
    public void setFilters(Filters value) { this.filters = value; }

    public List<String> getGroupings() { return groupings; }
    public void setGroupings(List<String> value) { this.groupings = value; }

    public List<HistoricalMetric> getHistoricalMetrics() { return historicalMetrics; }
    public void setHistoricalMetrics(List<HistoricalMetric> value) { this.historicalMetrics = value; }

    public long getMaxResults() { return maxResults; }
    public void setMaxResults(long value) { this.maxResults = value; }

    public String getNextToken() { return nextToken; }
    public void setNextToken(String value) { this.nextToken = value; }

    public long getStartTime() { return startTime; }
    public void setStartTime(long value) { this.startTime = value; }
}
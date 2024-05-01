package com.itesm.panoptimize.dto.dashboard.metric;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class RequestMetricDataV2 {
    @JsonProperty("end_time")
    private long end_time;
    @JsonProperty("filters")
    private List<Filter> filters;
    @JsonProperty("groupings")
    private List<String> groupings;
    @JsonProperty("interval")
    private Interval interval;
    @JsonProperty("max_results")
    private long maxResults;
    @JsonProperty("metrics")
    private List<Metric> metrics;
    @JsonProperty("next_token")
    private String nextToken;
    @JsonProperty("resource_arn")
    private String resource_arn;
    @JsonProperty("start_time")
    private long start_time;

    public long getEndTime() { return end_time; }
    public void setEndTime(long value) { this.end_time = value; }

    public List<Filter> getFilters() { return filters; }
    public void setFilters(List<Filter> value) { this.filters = value; }

    public List<String> getGroupings() { return groupings; }
    public void setGroupings(List<String> value) { this.groupings = value; }

    public Interval getInterval() { return interval; }
    public void setInterval(Interval value) { this.interval = value; }

    public long getMaxResults() { return maxResults; }
    public void setMaxResults(long value) { this.maxResults = value; }

    public List<Metric> getMetrics() { return metrics; }
    public void setMetrics(List<Metric> value) { this.metrics = value; }

    public String getNextToken() { return nextToken; }
    public void setNextToken(String value) { this.nextToken = value; }

    public String getResourceArn() { return resource_arn; }
    public void setResourceArn(String value) { this.resource_arn = value; }

    public long getStartTime() { return start_time; }
    public void setStartTime(long value) { this.start_time = value; }
}
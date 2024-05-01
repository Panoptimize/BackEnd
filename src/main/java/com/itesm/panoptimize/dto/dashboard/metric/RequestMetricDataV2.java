package com.itesm.panoptimize.dto.dashboard.metric;

public class RequestMetricDataV2 {
    private long endTime;
    private Filter[] filters;
    private String[] groupings;
    private Interval interval;
    private long maxResults;
    private Metric[] metrics;
    private String nextToken;
    private String resourceArn;
    private long startTime;


    public long getEndTime() { return endTime; }
    public void setEndTime(long value) { this.endTime = value; }

    public Filter[] getFilters() { return filters; }
    public void setFilters(Filter[] value) { this.filters = value; }

    public String[] getGroupings() { return groupings; }
    public void setGroupings(String[] value) { this.groupings = value; }

    public Interval getInterval() { return interval; }
    public void setInterval(Interval value) { this.interval = value; }

    public long getMaxResults() { return maxResults; }
    public void setMaxResults(long value) { this.maxResults = value; }

    public Metric[] getMetrics() { return metrics; }
    public void setMetrics(Metric[] value) { this.metrics = value; }

    public String getNextToken() { return nextToken; }
    public void setNextToken(String value) { this.nextToken = value; }

    public String getResourceArn() { return resourceArn; }
    public void setResourceArn(String value) { this.resourceArn = value; }

    public long getStartTime() { return startTime; }
    public void setStartTime(long value) { this.startTime = value; }
}

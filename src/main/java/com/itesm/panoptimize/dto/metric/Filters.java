package com.itesm.panoptimize.dto.metric;

import java.util.List;

public class Filters {
    private List<String> channels;
    private List<String> queues;
    private List<String> routingProfiles;
    private List<String> routingStepExpressions;

    public List<String> getChannels() { return channels; }
    public void setChannels(List<String> value) { this.channels = value; }

    public List<String> getQueues() { return queues; }
    public void setQueues(List<String> value) { this.queues = value; }

    public List<String> getRoutingProfiles() { return routingProfiles; }
    public void setRoutingProfiles(List<String> value) { this.routingProfiles = value; }

    public List<String> getRoutingStepExpressions() { return routingStepExpressions; }
    public void setRoutingStepExpressions(List<String> value) { this.routingStepExpressions = value; }
}
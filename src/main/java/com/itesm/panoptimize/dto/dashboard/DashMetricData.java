package com.itesm.panoptimize.dto.dashboard;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class DashMetricData {
    @NotBlank
    private String instance_id;

    private String start_time;

    private String end_time;

    private List<String> metrics;

    public String getInstance_id() {return instance_id;}

    public void setInstance_id(String instance_id) {this.instance_id = instance_id;}

    public String getStart_time() {return start_time;}

    public void setStart_time(String start_time) {this.start_time=start_time;}

    public List<String> getMetrics() {return metrics;}

    public void setMetrics(List<String> metrics) {this.metrics = metrics;}

    public String getEnd_time() {return end_time;}

    public void setEnd_time(String end_time) {this.end_time = end_time;}
}

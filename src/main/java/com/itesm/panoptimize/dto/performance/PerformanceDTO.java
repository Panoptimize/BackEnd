package com.itesm.panoptimize.dto.performance;
import java.util.List;
import java.util.Map;

public class PerformanceDTO {
    private  String metricName;
    private  Double metricValue;

    public PerformanceDTO(String metricName, Double metricValue) {
        this.metricName = metricName;
        this.metricValue = metricValue;
    }

}


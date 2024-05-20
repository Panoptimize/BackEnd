package com.itesm.panoptimize.dto.global_metric;

import jakarta.validation.constraints.NotNull;

public class GlobalMetricCreateDTO {
    @NotNull(message = "The metric description cannot be null")
    String metricDescription;

    @NotNull(message = "The current value cannot be null")
    Float currentValue;

    @NotNull(message = "The status cannot be null")
    String status;

    @NotNull(message = "The company id cannot be null")
    Integer companyId;

    public String getMetricDescription() {
        return metricDescription;
    }

    public void setMetricDescription(String metricDescription) {
        this.metricDescription = metricDescription;
    }

    public Float getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(Float currentValue) {
        this.currentValue = currentValue;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

}

package com.itesm.panoptimize.dto.contact;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MetricDTO {
    @JsonProperty("Name")
    private String name;

    @JsonProperty("Unit")
    private String unit;

    // Getters y Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}

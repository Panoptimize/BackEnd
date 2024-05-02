package com.itesm.panoptimize.dto.dashboard.metric;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Filter {
    @JsonProperty("filter_key")
    private String filterKey;
    @JsonProperty("filter_values")
    private List<String> filterValues;

    public String getFilterKey() { return filterKey; }
    public void setFilterKey(String value) { this.filterKey = value; }

    public List<String> getFilterValues() { return filterValues; }
    public void setFilterValues(List<String> value) { this.filterValues = value; }
}

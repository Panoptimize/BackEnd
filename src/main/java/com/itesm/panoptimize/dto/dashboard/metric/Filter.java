package com.itesm.panoptimize.dto.dashboard.metric;

public class Filter {
    private String filterKey;
    private String[] filterValues;

    public String getFilterKey() { return filterKey; }
    public void setFilterKey(String value) { this.filterKey = value; }

    public String[] getFilterValues() { return filterValues; }
    public void setFilterValues(String[] value) { this.filterValues = value; }
}

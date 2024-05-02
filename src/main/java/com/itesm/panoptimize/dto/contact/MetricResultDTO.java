package com.itesm.panoptimize.dto.contact;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


public class MetricResultDTO {
    @JsonProperty("Dimensions")
    private DimensionDTO dimensions;

    @JsonProperty("Collections")
    private List<CollectionDTO> collections;

    // Getters y Setters
    public DimensionDTO getDimensions() {
        return dimensions;
    }

    public void setDimensions(DimensionDTO dimensions) {
        this.dimensions = dimensions;
    }

    public List<CollectionDTO> getCollections() {
        return collections;
    }

    public void setCollections(List<CollectionDTO> collections) {
        this.collections = collections;
    }
}

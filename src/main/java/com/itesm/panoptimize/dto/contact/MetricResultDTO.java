package com.itesm.panoptimize.dto.contact;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.itesm.panoptimize.dto.contact.DimensionDTO;
import com.itesm.panoptimize.dto.contact.CollectionDTO;
import com.itesm.panoptimize.dto.contact.MetricDTO;

import java.util.List;


public class MetricResultDTO {

    @JsonProperty("Collections")
    private List<CollectionDTO> collections;

    @JsonProperty("Dimensions")
    private DimensionDTO dimensions;

    // Getters y Setters

    public List<CollectionDTO> getCollections() {
        return collections;
    }

    public void setCollections(List<CollectionDTO> collections) {
        this.collections = collections;
    }

    public DimensionDTO getDimensions() {
        return dimensions;
    }

    public void setDimensions(DimensionDTO dimensions) {
        this.dimensions = dimensions;
    }
}
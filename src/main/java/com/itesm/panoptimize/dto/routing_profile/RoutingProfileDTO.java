package com.itesm.panoptimize.dto.routing_profile;

import jakarta.validation.constraints.NotNull;

public class RoutingProfileDTO {
    @NotNull(message = "Id is required")
    private String routingProfileId;
    @NotNull(message = "Name is required")
    private String name;
    @NotNull(message = "Company Id is required")
    private Integer companyId;

    public String getRoutingProfileId() {
        return routingProfileId;
    }

    public void setRoutingProfileId(String routingProfileId) {
        this.routingProfileId = routingProfileId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
}

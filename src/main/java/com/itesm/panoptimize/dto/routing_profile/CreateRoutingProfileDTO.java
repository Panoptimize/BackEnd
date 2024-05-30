package com.itesm.panoptimize.dto.routing_profile;

import jakarta.validation.constraints.NotNull;

public class CreateRoutingProfileDTO {

    @NotNull(message = "Id is required")
    private String id;
    @NotNull(message = "Name is required")
    private String name;
    @NotNull(message = "Company id is required")
    private String companyId;

    public @NotNull(message = "Id is required") String getId() {
        return id;
    }

    public void setId(@NotNull(message = "Id is required") String id) {
        this.id = id;
    }

    public @NotNull(message = "Company id is required") String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(@NotNull(message = "Company id is required") String companyId) {
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

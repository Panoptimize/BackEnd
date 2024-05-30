package com.itesm.panoptimize.dto.routing_profile;

import jakarta.validation.constraints.NotNull;

public class UpdateRoutingProfileDTO {
    @NotNull(message = "Name is required")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

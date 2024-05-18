package com.itesm.panoptimize.dto.instance;

import jakarta.validation.constraints.NotNull;

public class InstanceDTO {
    @NotNull(message = "Id cannot be null")
    private String id;
    @NotNull(message = "Name cannot be null")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

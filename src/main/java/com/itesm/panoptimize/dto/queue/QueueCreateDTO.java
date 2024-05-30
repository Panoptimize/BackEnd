package com.itesm.panoptimize.dto.queue;

import jakarta.validation.constraints.NotNull;

public class QueueCreateDTO {
    @NotNull(message = "The type name cannot be null")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.toLowerCase();
    }
}
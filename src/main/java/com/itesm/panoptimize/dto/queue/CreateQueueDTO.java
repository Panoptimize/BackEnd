package com.itesm.panoptimize.dto.queue;

import jakarta.validation.constraints.NotNull;

public class CreateQueueDTO {
    @NotNull(message = "The id cannot be null")
    private String id;
    @NotNull(message = "The name cannot be null")
    private String name;

    public String getId() {
        return id;
    }

    public void setId(@NotNull(message = "The id cannot be null") String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.toLowerCase();
    }
}
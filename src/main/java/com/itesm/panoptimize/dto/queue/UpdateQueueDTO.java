package com.itesm.panoptimize.dto.queue;

import jakarta.validation.constraints.NotNull;

public class UpdateQueueDTO {
    @NotNull(message = "The name cannot be null")
    private String name;

    public UpdateQueueDTO(String name) {
        this.name = name;
    }

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}
}

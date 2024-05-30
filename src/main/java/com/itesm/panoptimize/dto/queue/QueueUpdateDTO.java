package com.itesm.panoptimize.dto.queue;

import jakarta.validation.constraints.NotNull;

public class QueueUpdateDTO {
    @NotNull(message = "The name cannot be null")
    private String name;

    public QueueUpdateDTO(String name) {
        this.name = name;
    }

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}
}

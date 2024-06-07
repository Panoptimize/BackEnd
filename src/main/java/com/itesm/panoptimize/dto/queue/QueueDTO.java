package com.itesm.panoptimize.dto.queue;

import jakarta.validation.constraints.NotNull;

public class QueueDTO {
    @NotNull(message = "Id is required")
    private String id;
    @NotNull(message = "Name is required")
    private String name;

    public String getId(){ return id; }
    public void setId(String id){ this.id = id; }
    public String getName(){ return name; }
    public void setName(String name){ this.name = name; }
}

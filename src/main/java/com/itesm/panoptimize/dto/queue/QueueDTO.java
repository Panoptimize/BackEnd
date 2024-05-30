package com.itesm.panoptimize.dto.queue;

import jakarta.validation.constraints.NotNull;

public class QueueDTO {
    @NotNull(message = "Id is required")
    private String queueId;
    @NotNull(message = "Name is required")
    private String name;

    public String getQueueId(){ return queueId; }
    public void setQueueId(String queueId){ this.queueId = queueId; }
    public String getName(){ return name; }
    public void setName(String name){ this.name = name; }
}

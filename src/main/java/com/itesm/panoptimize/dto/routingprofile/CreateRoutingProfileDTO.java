package com.itesm.panoptimize.dto.routingprofile;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public class CreateRoutingProfileDTO {

    @NotNull(message = "Id is required")
    private String id;
    @NotNull(message = "Name is required")
    private String name;
    @NotNull(message = "Company id is required")
    private Integer companyId;
    @NotNull(message = "Queues are required")
    private List<String> queues;

    public @NotNull(message = "Id is required") String getId() {
        return id;
    }

    public void setId(@NotNull(message = "Id is required") String id) {
        this.id = id;
    }

    public @NotNull(message = "Company id is required") Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(@NotNull(message = "Company id is required") Integer companyId) {
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getQueues() {
        return queues;
    }

    public void setQueues(List<String> queues) {
        this.queues = queues;
    }
}

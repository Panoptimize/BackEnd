package com.itesm.panoptimize.dto.agent;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Data Transfer Object (DTO) for representing the status metrics results.
 * This class is used to deserialize JSON responses that contain metrics results
 * structured in nested collections.
 */
public class StatusDTO {
    @JsonProperty("status")
    private String status;

    @JsonProperty("numUsers")
    private double numUsers;

    public StatusDTO(String status, double numUsers) {
        this.status = status;
        this.numUsers = numUsers;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getNumUsers() {
        return numUsers;
    }

    public void setNumUsers(double numUsers) {
        this.numUsers = numUsers;
    }
}
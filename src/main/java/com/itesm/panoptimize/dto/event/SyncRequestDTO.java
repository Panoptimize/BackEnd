package com.itesm.panoptimize.dto.event;

import jakarta.validation.constraints.NotNull;

public class SyncRequestDTO {
    @NotNull(message = "Instance ID is required")
    private String instanceId;
    @NotNull(message = "User pattern is required")
    private String userPattern;

    public @NotNull(message = "Instance ID is required") String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(@NotNull(message = "Instance ID is required") String instanceId) {
        this.instanceId = instanceId;
    }

    public @NotNull(message = "User pattern is required") String getUserPattern() {
        return userPattern;
    }

    public void setUserPattern(@NotNull(message = "User pattern is required") String userPattern) {
        this.userPattern = userPattern;
    }
}

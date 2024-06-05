package com.itesm.panoptimize.dto.agent;

import jakarta.validation.constraints.NotNull;

public class AgentCreateDTO {
    private final Integer id = null;
    @NotNull(message = "ConnectId is required")
    private String connectId;
    @NotNull(message = "Email is required")
    private String email;
    @NotNull(message = "FullName is required")
    private String fullName;

    @NotNull(message = "RoutingProfileId is required")
    private String routingProfileId;
    private boolean canSwitch = true;

    @NotNull(message = "CompanyId is required")
    private Integer companyId;

    public Integer getId() {
        return null;
    }

    public String getConnectId() {
        return connectId;
    }

    public void setConnectId(String connectId) {
        this.connectId = connectId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }



    public String getRoutingProfileId() {
        return routingProfileId;
    }

    public void setRoutingProfileId(String routingProfileId) {
        this.routingProfileId = routingProfileId;
    }

    public boolean isCanSwitch() {
        return canSwitch;
    }

    public void setCanSwitch(boolean canSwitch) {
        this.canSwitch = canSwitch;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
}

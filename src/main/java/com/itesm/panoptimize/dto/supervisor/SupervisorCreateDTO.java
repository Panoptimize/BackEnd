package com.itesm.panoptimize.dto.supervisor;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public class SupervisorCreateDTO {
    private final Integer id = null;
    @NotNull(message = "ConnectId is required")
    private String connectId;
    @NotNull(message = "FirebaseId is required")
    private String firebaseId;
    @NotNull(message = "Email is required")
    private String email;
    @NotNull(message = "FullName is required")
    private String fullName;
    @NotNull(message = "RoutingProfileId is required")
    private String routingProfileId;

    @NotNull(message = "CompanyId is required")
    private Integer companyId;

    private List<Integer> agentIds;

    public Integer getId() {
        return null;
    }

    public String getConnectId() {
        return connectId;
    }

    public void setConnectId(String connectId) {
        this.connectId = connectId;
    }

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
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

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public List<Integer> getAgentIds() {
        return agentIds;
    }

    public void setAgentIds(List<Integer> agentIds) {
        this.agentIds = agentIds;
    }
}

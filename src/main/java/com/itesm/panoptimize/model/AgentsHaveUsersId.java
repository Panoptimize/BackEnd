package com.itesm.panoptimize.model;

import java.io.Serializable;
import java.util.Objects;

public class AgentsHaveUsersId implements Serializable {
    private int supervisorId;
    private int agentId;

    public int getSupervisorId() {
        return supervisorId;
    }

    public void setSupervisorId(int supervisorId) {
        this.supervisorId = supervisorId;
    }

    public int getAgentId() {
        return agentId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AgentsHaveUsersId that = (AgentsHaveUsersId) o;
        return supervisorId == that.supervisorId && agentId == that.agentId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(supervisorId, agentId);
    }
}

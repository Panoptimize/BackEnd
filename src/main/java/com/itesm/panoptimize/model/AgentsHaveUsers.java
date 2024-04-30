package com.itesm.panoptimize.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "agents_have_users")
@IdClass(AgentsHaveUsersId.class)
public class AgentsHaveUsers {

    @Id
    @Column(name = "supervisor_id", nullable = false)
    private int supervisorId;

    @Id
    @Column(name = "agent_id", nullable = false)
    private int agentId;

    @ManyToOne
    @JoinColumn(name = "supervisor_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    private User supervisor;

    @ManyToOne
    @JoinColumn(name = "agent_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    private User agent;

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

    public User getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(User supervisor) {
        this.supervisor = supervisor;
    }

    public User getAgent() {
        return agent;
    }

    public void setAgent(User agent) {
        this.agent = agent;
    }
}


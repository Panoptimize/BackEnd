package com.itesm.panoptimize.model;

import jakarta.persistence.*;

import java.io.Serializable;

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
}

class AgentsHaveUsersId implements Serializable {
    private int supervisorId;
    private int agentId;
}

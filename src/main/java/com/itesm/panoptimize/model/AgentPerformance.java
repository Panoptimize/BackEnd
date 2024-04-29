package com.itesm.panoptimize.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "agent_performance")
public class AgentPerformance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "agent_performance_id")
    private int id;

    @Column(nullable = false)
    private Date date;

    @Column(name = "total_contacts_handled", nullable = false)
    private int totalContactsHandled;

    @Column(name = "total_after_callwork", nullable = false)
    private int totalAfterCallwork;

    @Column(name = "adherence_percentage", nullable = false)
    private float adherencePercentage;

    @ManyToOne
    @JoinColumn(name = "agent_id", nullable = false)
    private User agent;
}
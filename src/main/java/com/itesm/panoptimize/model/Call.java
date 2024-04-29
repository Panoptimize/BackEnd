package com.itesm.panoptimize.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "`call`")
public class Call {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "call_id")
    private int id;

    @Column(name = "start_time", nullable = false)
    private Date startTime;

    @Column(name = "end_time", nullable = false)
    private Date endTime;

    @Column(name = "resolution_status", nullable = false)
    private String resolutionStatus;

    @Column(name = "first_contact_resolution", nullable = false)
    private boolean firstContactResolution;

    @Column(name = "sentiment_negative", nullable = false)
    private float sentimentNegative;

    @Column(name = "sentiment_positive", nullable = false)
    private float sentimentPositive;

    @ManyToOne
    @JoinColumn(name = "agent_id", nullable = false)
    private User agent;
}

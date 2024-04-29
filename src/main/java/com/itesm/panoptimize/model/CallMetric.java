package com.itesm.panoptimize.model;

import jakarta.persistence.*;

@Entity
@Table(name = "call_metric")
public class CallMetric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "metric_id")
    private int id;

    @Column(name = "speed_of_answer", nullable = false)
    private int speedOfAnswer;

    @Column(name = "handle_time", nullable = false)
    private int handleTime;

    @Column(name = "after_callwork_time", nullable = false)
    private int afterCallworkTime;

    @Column(nullable = false)
    private boolean abandoned;

    @ManyToOne
    @JoinColumn(name = "call_call_id", nullable = false)
    private Call call;
}

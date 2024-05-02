package com.itesm.panoptimize.model;

import jakarta.persistence.*;

@Entity
@Table(name = "satisfaction_level")
public class SatisfactionLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "satisfaction_level_id")
    private int id;

    @Column(name = "level_name", nullable = false)
    private String levelName;

    @ManyToOne
    @JoinColumn(name = "contact_id", nullable = false)
    private Contact contact;
}
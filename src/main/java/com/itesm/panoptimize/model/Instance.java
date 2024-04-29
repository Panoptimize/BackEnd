package com.itesm.panoptimize.model;


import jakarta.persistence.*;

@Entity
@Table(name = "instance")
public class Instance {
    @Id
    @Column(name = "instance_id", nullable = false)
    private String id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
}


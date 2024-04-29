package com.itesm.panoptimize.model;

import jakarta.persistence.*;

@Entity
@Table(name = "company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    private Long companyId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "slogan")
    private String slogan;

    @Column(name = "logo_path")
    private String logoPath;

    // Getters and setters
}
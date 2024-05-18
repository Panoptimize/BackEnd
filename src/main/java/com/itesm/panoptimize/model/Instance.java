package com.itesm.panoptimize.model;


import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "instance")
public class Instance {
    @Id
    @Column(name = "instance_id", nullable = false, length = 36)
    private String id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}


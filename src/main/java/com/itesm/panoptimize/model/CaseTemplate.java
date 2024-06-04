package com.itesm.panoptimize.model;

import jakarta.persistence.*;

@Entity
@Table(name = "case_template")
public class CaseTemplate {
    @Id
    @Column(name = "case_template_id", length = 36)
    private String id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @ManyToOne
    @JoinColumn(name = "company_id", foreignKey = @ForeignKey(name = "case_template_has_company"))
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

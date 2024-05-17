package com.itesm.panoptimize.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "company", indexes = {
        @Index(name = "company_name_index", columnList = "name")
})
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    private Integer companyId;

    @Column(name = "name", nullable = false, length = 10)
    private String name;

    @Column(name = "slogan", length = 50)
    private String slogan;

    @Column(name = "logo_path", length = 100)
    private String logoPath;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private Set<User> users;

    // Getters and setters


    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
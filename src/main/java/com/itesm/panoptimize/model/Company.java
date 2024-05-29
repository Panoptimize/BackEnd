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
    private Integer id;

    @Column(name = "name", nullable = false, length = 20, unique = true)
    private String name;

    @Column(name = "slogan", length = 50)
    private String slogan;

    @Column(name = "logo_path", length = 100, unique = true)
    private String logoPath;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private Set<User> users;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private Set<RoutingProfile> routingProfiles;

    public Integer getId() {
        return id;
    }

    public void setId(Integer companyId) {
        this.id = companyId;
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

    public Set<RoutingProfile> getRoutingProfiles() {
        return routingProfiles;
    }

    public void setRoutingProfiles(Set<RoutingProfile> routingProfiles) {
        this.routingProfiles = routingProfiles;
    }
}
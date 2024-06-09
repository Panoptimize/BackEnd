package com.itesm.panoptimize.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "routing_profile")
public class RoutingProfile {
    @Id
    @Column(name = "routing_profile_id", length = 36)
    private java.lang.String routingProfileId;

    @Column(name = "name", nullable = false, length = 50)
    private java.lang.String name;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false, foreignKey = @ForeignKey(name = "company_has_routing_profiles"))
    private Company company;

    @OneToMany(mappedBy = "routingProfile")
    private Set<User> users;

    @ManyToMany
    @JoinTable(
            name = "routing_profiles_have_queues",
            joinColumns = @JoinColumn(name = "routing_profile_id", foreignKey = @ForeignKey(name = "routing_profiles_have_queues")),
            inverseJoinColumns = @JoinColumn(name = "queue_id", foreignKey = @ForeignKey(name = "queues_have_routing_profiles"))
    )
    private Set<Queue> queues;

    public Set<Queue> getQueues() {
        return queues;
    }

    public void setQueues(Set<Queue> queues) {
        this.queues = queues;
    }

    public java.lang.String getRoutingProfileId() {
        return routingProfileId;
    }

    public void setRoutingProfileId(java.lang.String routingProfileId) {
        this.routingProfileId = routingProfileId;
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company string) {
        this.company = string;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}

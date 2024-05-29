package com.itesm.panoptimize.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "queue")
public class Queue {
    @Id
    @Column(name = "quque_id")
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany
    @JoinTable(
            name = "routing_profiles_have_queues",
            joinColumns = @JoinColumn(name = "queue_id"),
            inverseJoinColumns = @JoinColumn(name = "routing_profile_id")
    )
    private Set<RoutingProfile> routingProfiles;

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

    public Set<RoutingProfile> getRoutingProfiles() {
        return routingProfiles;
    }

    public void setRoutingProfiles(Set<RoutingProfile> routingProfiles) {
        this.routingProfiles = routingProfiles;
    }
}

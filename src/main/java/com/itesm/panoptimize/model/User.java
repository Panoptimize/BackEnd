package com.itesm.panoptimize.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;

    @Column(name = "connect_id", nullable = false, unique = true)
    private String connectId;

    @Column(name = "firebase_id")
    private String firebaseId;

    @Column(nullable = false)
    private String email;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "can_switch", nullable = false)
    private Boolean canSwitch = true;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false, foreignKey = @ForeignKey(name = "company_has_users"))
    private Company company;

    @ManyToOne
    @JoinColumn(name = "user_type_id", nullable = false, foreignKey = @ForeignKey(name = "user_has_user_type"))
    private UserType userType;

    @ManyToMany
    @JoinTable(
            name = "agents_have_supervisors",
            joinColumns = @JoinColumn(name = "agent_id", foreignKey = @ForeignKey(name = "agents_have_supervisors")),
            inverseJoinColumns = @JoinColumn(name = "supervisor_id", foreignKey = @ForeignKey(name = "supervisors_have_agents"))
    )
    private Set<User> supervisors;

    @ManyToMany(mappedBy = "supervisors")
    private Set<User> agents;

    @ManyToOne
    @JoinColumn(name = "routing_profile_id", nullable = false, foreignKey = @ForeignKey(name = "user_has_routing_profile"))
    private RoutingProfile routingProfile;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getConnectId() {
        return connectId;
    }

    public void setConnectId(String connectId) {
        this.connectId = connectId;
    }

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public RoutingProfile getRoutingProfile() {
        return routingProfile;
    }

    public void setRoutingProfile(RoutingProfile routingProfileId) {
        this.routingProfile = routingProfileId;
    }

    public boolean isCanSwitch() {
        return canSwitch;
    }

    public void setCanSwitch(boolean canSwitch) {
        this.canSwitch = canSwitch;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public Set<User> getSupervisors() {
        return supervisors;
    }

    public void setSupervisors(Set<User> supervisors) {
        this.supervisors = supervisors;
    }

    public Set<User> getAgents() {
        return agents;
    }

    public void setAgents(Set<User> agents) {
        this.agents = agents;
    }
}
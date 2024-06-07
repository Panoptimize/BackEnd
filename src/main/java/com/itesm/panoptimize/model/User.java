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

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false, foreignKey = @ForeignKey(name = "company_has_users"))
    private Company company;

    @ManyToOne
    @JoinColumn(name = "user_type_id", nullable = false, foreignKey = @ForeignKey(name = "user_has_user_type"))
    private UserType userType;

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

    public RoutingProfile getRoutingProfile() {
        return routingProfile;
    }

    public void setRoutingProfile(RoutingProfile routingProfileId) {
        this.routingProfile = routingProfileId;
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
}
package com.itesm.panoptimize.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id;

    @Column(name = "connect_id", nullable = false, unique = true)
    private String connectId;

    @Column(name = "firebase_id", nullable = false, unique = true)
    private UUID firebaseId;

    @Column(nullable = false)
    private String email;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "routing_profile_id")
    private String routing_profile_id;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne
    @JoinColumn(name = "user_type_id", nullable = false)
    private UserType userType;

    public int getId() {
        return id;
    }

    public String getConnectId() {
        return connectId;
    }

    public void setConnectId(String connectId) {
        this.connectId = connectId;
    }

    public UUID getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(UUID firebaseId) { this.firebaseId = firebaseId; }

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

    public String getRouting_profile_id() {
        return routing_profile_id;
    }

    public void setRouting_profile_id(String routing_profile_id) {
        this.routing_profile_id = routing_profile_id;
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
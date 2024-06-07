package com.itesm.panoptimize.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "user_type")
public class UserType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_type_id")
    private Integer id;

    @Column(name = "user_type_name", nullable = false, unique = true, length = 10)
    private String typeName;

    @Column(name = "security_profile_id", nullable = false, length = 36, unique = true)
    private String securityProfileId;

    @OneToMany(mappedBy = "userType")
    private Set<User> users;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public String getSecurityProfileId() {
        return securityProfileId;
    }

    public void setSecurityProfileId(String securityProfileId) {
        this.securityProfileId = securityProfileId;
    }
}
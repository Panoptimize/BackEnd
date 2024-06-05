package com.itesm.panoptimize.dto.supervisor;

import com.itesm.panoptimize.model.Company;
import com.itesm.panoptimize.model.UserType;
import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

public class SupervisorUpdateDTO {
    private Integer id;

    private String connectId;

    private String firebaseId;

    private String email;

    private String fullName;



    private String routingProfileId;

    private Boolean canSwitch;

    private Company company;

    private UserType userType;

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



    public String getRoutingProfileId() {
        return routingProfileId;
    }

    public void setRoutingProfileId(String routingProfileId) {
        this.routingProfileId = routingProfileId;
    }

    public Boolean isCanSwitch() {
        return canSwitch;
    }

    public void setCanSwitch(Boolean canSwitch) {
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
}

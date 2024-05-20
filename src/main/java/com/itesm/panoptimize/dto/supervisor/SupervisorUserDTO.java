package com.itesm.panoptimize.dto.supervisor;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;

public class SupervisorUserDTO {
    @NotNull(message = "Id is required")
    private Integer id;
    @NotNull(message = "ConnectId is required")
    private String connectId;
    @NotNull(message = "FirebaseId is required")
    private String firebaseId;
    @NotNull(message = "Email is required")
    private String email;
    @NotNull(message = "FullName is required")
    private String fullName;
    private String imagePath;
    @NotNull(message = "RoutingProfileId is required")
    private String routingProfileId;
    private boolean canSwitch = true;

    @NotNull(message = "CompanyId is required")
    private Integer companyId;

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

    public String getRoutingProfileId() {
        return routingProfileId;
    }

    public void setRoutingProfileId(String routingProfileId) {
        this.routingProfileId = routingProfileId;
    }

    public boolean isCanSwitch() {
        return canSwitch;
    }

    public void setCanSwitch(boolean canSwitch) {
        this.canSwitch = canSwitch;
    }
}

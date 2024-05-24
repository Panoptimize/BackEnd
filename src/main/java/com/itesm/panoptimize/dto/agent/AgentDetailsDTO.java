package com.itesm.panoptimize.dto.agent;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class AgentDetailsDTO {

    private String directoryUserId;
    private String hierarchyGroupId;
    private String id;
    private IdentityInfoDTO identityInfo;
    private Instant lastModifiedTime;

    private String routingProfileId;

    private String username;

    // Getters and Setters


    public String getDirectoryUserId() {
        return directoryUserId;
    }

    public void setDirectoryUserId(String directoryUserId) {
        this.directoryUserId = directoryUserId;
    }

    public String getHierarchyGroupId() {
        return hierarchyGroupId;
    }

    public void setHierarchyGroupId(String hierarchyGroupId) {
        this.hierarchyGroupId = hierarchyGroupId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public IdentityInfoDTO getIdentityInfo() {
        return identityInfo;
    }

    public void setIdentityInfo(IdentityInfoDTO identityInfo) {
        this.identityInfo = identityInfo;
    }


    public Instant getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(Instant lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public String getRoutingProfileId() {
        return routingProfileId;
    }

    public void setRoutingProfileId(String routingProfileId) {
        this.routingProfileId = routingProfileId;
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}


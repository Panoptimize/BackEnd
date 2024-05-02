package com.itesm.panoptimize.dto.supervisor;

import java.util.List;

import com.itesm.panoptimize.dto.agent.AgentDTO;

public class SupervisorDTO {
    private String name;
    private String email;
    private String username;
    private String password;
    private String floor;
    private boolean verified;
    private String picture;
    private List<AgentDTO> supervisedAgents;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<AgentDTO> getSupervisedAgents() {
        return supervisedAgents;
    }

    public void setSupervisedAgents(List<AgentDTO> supervisedAgents) {
        this.supervisedAgents = supervisedAgents;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}


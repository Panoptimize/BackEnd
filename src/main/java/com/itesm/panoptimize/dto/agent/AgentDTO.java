package com.itesm.panoptimize.dto.agent;

import java.util.List;
public class AgentDTO {
    private int id; //ID to compare and fetch info
    private String name;
    private String emailID;
    private String username;
    private String password;
    private List <WorkspaceDTO> workspaces; //or workspace

    //Getters and Setters


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

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
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

    public List<WorkspaceDTO> getWorkspaces() {
        return workspaces;
    }

    public void setWorkspaces(List<WorkspaceDTO> workspaces) {
        this.workspaces = workspaces;
    }
}

//We need to check how to properly check this and send the info that is necesary (not passwords or
//fragile information)
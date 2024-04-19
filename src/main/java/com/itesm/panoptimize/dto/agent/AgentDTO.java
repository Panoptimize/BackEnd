package com.itesm.panoptimize.dto.agent;

public class AgentDTO {
    private int id;
    private String name;
    private String workspace;
    private String currentContactM;
    private int intTemperature;

    public AgentDTO(String name, String workspace, String currentContactM, int intTemperature) {
        this.name = name;
        this.workspace = workspace;
        this.currentContactM = currentContactM;
        this.intTemperature = intTemperature;
    }

    public AgentDTO() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWorkspace() {
        return workspace;
    }

    public void setWorkspace(String workspace) {
        this.workspace = workspace;
    }

    public String getCurrentContactM() {
        return currentContactM;
    }

    public void setCurrentContactM(String currentContactM) {
        this.currentContactM = currentContactM;
    }

    public int getIntTemperature() {
        return intTemperature;
    }

    public void setIntTemperature(int intTemperature) {
        this.intTemperature = intTemperature;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
package com.itesm.panoptimize.dto.note;

import com.itesm.panoptimize.enumerator.Priority;

public class UpdateNoteDTO {
    private String name;
    private String description;
    private Priority priority;
    private Boolean solved;
    private Integer agentPerformanceId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Boolean getSolved() {
        return solved;
    }

    public void setSolved(Boolean solved) {
        this.solved = solved;
    }

    public Integer getAgentPerformanceId() {
        return agentPerformanceId;
    }

    public void setAgentPerformanceId(Integer agentPerformanceId) {
        this.agentPerformanceId = agentPerformanceId;
    }
}

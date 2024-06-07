package com.itesm.panoptimize.dto.event;

import com.itesm.panoptimize.dto.agent.AgentUserDTO;
import com.itesm.panoptimize.dto.supervisor.SupervisorUserDTO;

import java.util.List;

public class CreatedUsersDTO {
    List<AgentUserDTO> agents;
    List<SupervisorUserDTO> supervisors;

    public List<AgentUserDTO> getAgents() {
        return agents;
    }

    public void setAgents(List<AgentUserDTO> agents) {
        this.agents = agents;
    }

    public List<SupervisorUserDTO> getSupervisors() {
        return supervisors;
    }

    public void setSupervisors(List<SupervisorUserDTO> supervisors) {
        this.supervisors = supervisors;
    }
}

package com.itesm.panoptimize.dto.agent;

import java.util.List;

public class AgentResponseDTO {
    private List<AgentListDTO> AGENTS;

    public AgentResponseDTO(List<AgentListDTO> AGENTS) {
        this.AGENTS = AGENTS;
    }

    public List<AgentListDTO> getAGENTS() {
        return AGENTS;
    }

    public void setAGENTS(List<AgentListDTO> AGENTS) {
        this.AGENTS = AGENTS;
    }
}


package com.itesm.panoptimize.dto.agent_performance;

import com.itesm.panoptimize.dto.note.CreateNoteDTO;
import software.amazon.awssdk.annotations.NotNull;

public class CreateAgentPerformanceWithNote {
    @NotNull
    private CreateNoteDTO createNoteDTO;
    @NotNull
    private CreateAgentPerformanceDTO createAgentPerformanceDTO;

    public CreateNoteDTO getCreateNoteDTO() {
        return createNoteDTO;
    }

    public void setCreateNoteDTO(CreateNoteDTO createNoteDTO) {
        this.createNoteDTO = createNoteDTO;
    }

    public CreateAgentPerformanceDTO getCreateAgentPerformanceDTO() {
        return createAgentPerformanceDTO;
    }

    public void setCreateAgentPerformanceDTO(CreateAgentPerformanceDTO createAgentPerformanceDTO) {
        this.createAgentPerformanceDTO = createAgentPerformanceDTO;
    }
}

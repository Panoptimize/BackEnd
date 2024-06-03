package com.itesm.panoptimize.dto.agent_performance;

import com.itesm.panoptimize.dto.note.CreateNoteDTO;
import software.amazon.awssdk.annotations.NotNull;

public class CreateAgentPerformanceWithNote {
    @NotNull
    private CreateNoteDTO createNoteDTO;
    @NotNull
    private CreateAgent_PerformanceDTO createAgent_PerformanceDTO;

    public CreateNoteDTO getCreateNoteDTO() {
        return createNoteDTO;
    }

    public void setCreateNoteDTO(CreateNoteDTO createNoteDTO) {
        this.createNoteDTO = createNoteDTO;
    }

    public CreateAgent_PerformanceDTO getCreateAgent_PerformanceDTO() {
        return createAgent_PerformanceDTO;
    }

    public void setCreateAgent_PerformanceDTO(CreateAgent_PerformanceDTO createAgent_PerformanceDTO) {
        this.createAgent_PerformanceDTO = createAgent_PerformanceDTO;
    }
}

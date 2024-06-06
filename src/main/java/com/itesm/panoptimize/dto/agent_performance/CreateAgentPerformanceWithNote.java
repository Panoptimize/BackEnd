package com.itesm.panoptimize.dto.agent_performance;

import com.itesm.panoptimize.dto.note.CreateNoteDTO;
import software.amazon.awssdk.annotations.NotNull;

public class CreateAgentPerformanceWithNote {
    @NotNull
    private CreateNoteDTO createNote;
    @NotNull
    private CreateAgentPerformanceDTO createAgentPerformance;

    public CreateNoteDTO getCreateNote() {
        return createNote;
    }

    public void setCreateNote(CreateNoteDTO createNote) {
        this.createNote = createNote;
    }

    public CreateAgentPerformanceDTO getCreateAgentPerformance() {
        return createAgentPerformance;
    }

    public void setCreateAgentPerformance(CreateAgentPerformanceDTO createAgentPerformance) {
        this.createAgentPerformance = createAgentPerformance;
    }
}

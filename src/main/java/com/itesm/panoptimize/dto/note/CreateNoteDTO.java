package com.itesm.panoptimize.dto.note;

import com.itesm.panoptimize.enumerator.Priority;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public class CreateNoteDTO {
    @NotNull(message = "The name cannot be null")
    private String name;
    @NotNull(message = "The description cannot be null")
    private String description;
    @NotNull(message = "The priority cannot be null")
    private Priority priority;
    @NotNull
    private Boolean solved;

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
}

package com.itesm.panoptimize.dto.case_template;

import jakarta.validation.constraints.NotNull;

public class CreateCaseTemplateDTO {
    @NotNull(message = "The type name cannot be null")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.toLowerCase();
    }
}

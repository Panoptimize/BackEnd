package com.itesm.panoptimize.dto.user_type;

import jakarta.validation.constraints.NotNull;

public class UserTypeCreateDTO {
    @NotNull(message = "The type name cannot be null")
    private String typeName;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}

package com.itesm.panoptimize.dto.user_type;

import jakarta.validation.constraints.NotNull;

public class UserTypeDTO {
    @NotNull(message = "Id is required")
    private Integer id;
    @NotNull(message = "Type is required")
    private String type;
    @NotNull(message = "SecurityProfileId is required")
    private String securityProfileId;

    public UserTypeDTO() {
    }

    public UserTypeDTO(Integer id, String type) {
        this.id = id;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }
}

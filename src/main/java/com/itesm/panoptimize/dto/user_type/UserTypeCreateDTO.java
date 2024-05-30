package com.itesm.panoptimize.dto.user_type;

import jakarta.validation.constraints.NotNull;

public class UserTypeCreateDTO {
    private final Integer id = null;
    @NotNull(message = "The type name cannot be null")
    private String typeName;
    @NotNull(message = "The security profile id cannot be null")
    private String securityProfileId;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName.toLowerCase();
    }

    public @NotNull(message = "The security profile id cannot be null") String getSecurityProfileId() {
        return securityProfileId;
    }

    public void setSecurityProfileId(@NotNull(message = "The security profile id cannot be null") String securityProfileId) {
        this.securityProfileId = securityProfileId;
    }

    public Integer getId() {
        return id;
    }
}

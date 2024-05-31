package com.itesm.panoptimize.dto.user_type;

public class UserTypeUpdateDTO {
    private Integer id;
    private String type;
    private String securityProfileId;

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

    public String getSecurityProfileId() {
        return securityProfileId;
    }

    public void setSecurityProfileId(String securityProfileId) {
        this.securityProfileId = securityProfileId;
    }
}

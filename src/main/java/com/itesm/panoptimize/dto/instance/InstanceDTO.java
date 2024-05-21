package com.itesm.panoptimize.dto.instance;

import jakarta.validation.constraints.NotNull;

public class InstanceDTO {
    @NotNull(message = "Id cannot be null")
    private String id;
    @NotNull(message = "Name cannot be null")
    private String name;
    @NotNull(message = "Company id cannot be null")
    private String companyId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}

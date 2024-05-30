package com.itesm.panoptimize.dto.case_template;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class CaseTemplateDTO {
    Integer caseTemplateId;
    @NotNull(message = "Name is required")
    @NotEmpty(message = "Name is required")
    private String name;

    Integer companyId;

    public Integer getCaseTemplateId() {
        return caseTemplateId;
    }

    public void setCaseTemplateId(Integer caseTemplateId) {
        this.caseTemplateId = caseTemplateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
}

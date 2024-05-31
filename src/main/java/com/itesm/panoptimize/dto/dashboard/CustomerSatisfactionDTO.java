package com.itesm.panoptimize.dto.dashboard;

import java.util.List;

public class CustomerSatisfactionDTO {
    private List<Long> satisfaction_levels;

    public List<Long> getSatisfaction_levels() {
        return satisfaction_levels;
    }

    public void setSatisfaction_levels(List<Long> satisfaction_levels) {
        this.satisfaction_levels = satisfaction_levels;
    }
}

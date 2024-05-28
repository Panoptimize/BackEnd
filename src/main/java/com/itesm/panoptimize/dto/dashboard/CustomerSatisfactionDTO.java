package com.itesm.panoptimize.dto.dashboard;

import java.util.List;

public class CustomerSatisfactionDTO {
    private List<Integer> satisfaction_levels;

    public List<Integer> getSatisfaction_levels() {
        return satisfaction_levels;
    }

    public void setSatisfaction_levels(List<Integer> satisfaction_levels) {
        this.satisfaction_levels = satisfaction_levels;
    }
}

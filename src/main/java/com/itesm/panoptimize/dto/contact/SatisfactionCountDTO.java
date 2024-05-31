package com.itesm.panoptimize.dto.contact;

public class SatisfactionCountDTO {
    private Integer satisfaction;
    private Long count;

    public SatisfactionCountDTO(Integer satisfaction, Long count) {
        this.satisfaction = satisfaction;
        this.count = count;
    }

    // Getters and Setters
    public Integer getSatisfaction() {
        return satisfaction;
    }

    public void setSatisfaction(Integer satisfaction) {
        this.satisfaction = satisfaction;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}

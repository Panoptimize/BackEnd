package com.itesm.panoptimize.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "global_metric")
public class GlobalMetric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "global_metric_id")
    private int id;

    @Column(name = "metric_description", nullable = false)
    private String metricDescription;

    @Column(name = "current_value", nullable = false)
    private float currentValue;

    @Column(nullable = false)
    private String status;

    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMetricDescription() {
        return metricDescription;
    }

    public void setMetricDescription(String metricDescription) {
        this.metricDescription = metricDescription;
    }

    public float getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(float currentValue) {
        this.currentValue = currentValue;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}

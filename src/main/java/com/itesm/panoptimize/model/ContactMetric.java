package com.itesm.panoptimize.model;

import jakarta.persistence.*;

@Entity
@Table(name = "contact_metric")
public class ContactMetric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "metric_id")
    private Integer contactMetricId;

    @Column(name = "speed_of_answer")
    private Integer speedOfAnswer;

    @Column(name = "handle_time")
    private Integer handleTime;

    @Column(name = "after_call_work_time")
    private Integer afterCallWorkTime;

    @Column(name = "abandoned")
    private Boolean abandoned = false;

    @Column(name = "first_contact_resolution")
    private Boolean firstContactResolution = true;

    @Column(name = "sentiment_negative")
    private Float sentimentNegative;

    @Column(name = "sentiment_positive")
    private Float sentimentPositive;

    @ManyToOne
    @JoinColumn(name = "contact_id", nullable = false)
    private Contact contact;

    @Column(name = "contact_status", length = 10, nullable = false)
    private Integer contactStatus;

    public Integer getContactMetricId() {
        return contactMetricId;
    }

    public void setContactMetricId(Integer contactMetricId) {
        this.contactMetricId = contactMetricId;
    }

    public Integer getSpeedOfAnswer() {
        return speedOfAnswer;
    }

    public void setSpeedOfAnswer(Integer speedOfAnswer) {
        this.speedOfAnswer = speedOfAnswer;
    }

    public Integer getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(Integer handleTime) {
        this.handleTime = handleTime;
    }

    public Integer getAfterCallWorkTime() {
        return afterCallWorkTime;
    }

    public void setAfterCallWorkTime(Integer afterCallWorkTime) {
        this.afterCallWorkTime = afterCallWorkTime;
    }

    public Boolean getAbandoned() {
        return abandoned;
    }

    public void setAbandoned(Boolean abandoned) {
        this.abandoned = abandoned;
    }

    public Boolean getFirstContactResolution() {
        return firstContactResolution;
    }

    public void setFirstContactResolution(Boolean firstContactResolution) {
        this.firstContactResolution = firstContactResolution;
    }

    public Float getSentimentNegative() {
        return sentimentNegative;
    }

    public void setSentimentNegative(Float sentimentNegative) {
        this.sentimentNegative = sentimentNegative;
    }

    public Float getSentimentPositive() {
        return sentimentPositive;
    }

    public void setSentimentPositive(Float sentimentPositive) {
        this.sentimentPositive = sentimentPositive;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Integer getContactStatus() {
        return contactStatus;
    }

    public void setContactStatus(Integer contactStatus) {
        this.contactStatus = contactStatus;
    }
}
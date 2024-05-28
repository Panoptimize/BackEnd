package com.itesm.panoptimize.model;

import com.itesm.panoptimize.enumerator.SatisfactionLevel;
import jakarta.persistence.*;

import java.util.Date;
import java.util.Set;

/**
 * Entity representing a contact.
 * This class maps to the 'contact' table in the database and defines the structure for contact data.
 */
@Entity
@Table(name = "contact")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contact_id")
    private Integer id;

    @Column(name = "start_time")
    private Date startTime;

    @Column(name = "end_time")
    private Date endTime;

    @ManyToOne
    @JoinColumn(name = "agent_id", nullable = false)
    private User agent;

    @Enumerated(EnumType.STRING)
    @Column(name = "satisfaction_level")
    private SatisfactionLevel satisfactionLevel;

    @OneToOne(mappedBy = "contact")
    private ContactMetric contactMetrics;

    @OneToMany(mappedBy = "contact")
    private Set<Feedback> feedbacks;

    @OneToMany(mappedBy = "contact")
    private Set<Notification> notifications;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public User getAgent() {
        return agent;
    }

    public void setAgent(User agent) {
        this.agent = agent;
    }

    public SatisfactionLevel getSatisfactionLevel() {
        return satisfactionLevel;
    }

    public void setSatisfactionLevel(SatisfactionLevel satisfactionLevel) {
        this.satisfactionLevel = satisfactionLevel;
    }

    public ContactMetric getContactMetrics() {
        return contactMetrics;
    }

    public void setContactMetrics(ContactMetric contactMetrics) {
        this.contactMetrics = contactMetrics;
    }

    public Set<Feedback> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(Set<Feedback> feedbacks) {
        this.feedbacks = feedbacks;
    }

    public Set<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(Set<Notification> notifications) {
        this.notifications = notifications;
    }

}
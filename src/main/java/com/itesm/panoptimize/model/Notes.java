package com.itesm.panoptimize.model;

import com.itesm.panoptimize.enumerator.Priority;
import jakarta.persistence.*;
import org.hibernate.annotations.Collate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "notes")
public class Notes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id")
    private Integer id;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Column(name = "description", nullable = false, length = 255)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private Priority priority;

    @Column(name = "solved", nullable = false)
    private Boolean solved;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @ManyToOne
    @JoinColumn(name = "agent_id")
    private User user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer feedbackId) {
        this.id = feedbackId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public Instant getCreated_at() {
        return createdAt;
    }

    public void setCreated_at(Instant created_at) {
        this.createdAt = created_at;
    }

    public Instant getUpdated_at() {
        return updatedAt;
    }

    public void setUpdated_at(Instant updated_at) {
        this.updatedAt = updated_at;
    }
}
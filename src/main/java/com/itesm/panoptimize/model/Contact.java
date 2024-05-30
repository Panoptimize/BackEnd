package com.itesm.panoptimize.model;

import jakarta.persistence.*;

/**
 * Entity representing a contact.
 * This class maps to the 'contact' table in the database and defines the structure for contact data.
 */
@Entity
@Table(name = "contact", indexes = {
        @Index(name = "contact_satisfaction_index", columnList = "satisfaction")
})
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contact_id")
    private Integer id;

    @Column(name = "satisfaction")
    private Integer satisfaction;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "contact_has_user"))
    private User user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSatisfaction() {
        return satisfaction;
    }

    public void setSatisfaction(Integer satisfaction) {
        this.satisfaction = satisfaction;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
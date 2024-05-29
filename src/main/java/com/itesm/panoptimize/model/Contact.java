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

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User userId;

    @Column(name = "satisfaction")
    private Integer satisfaction;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser_id() {
        return userId;
    }

    public void setUser_id(User user_id) {
        this.userId = user_id;
    }

    public Integer getSatisfaction() {
        return satisfaction;
    }

    public void setSatisfaction(Integer satisfaction) {
        this.satisfaction = satisfaction;
    }
}
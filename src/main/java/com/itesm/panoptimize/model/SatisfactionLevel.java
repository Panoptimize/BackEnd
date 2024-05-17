package com.itesm.panoptimize.model;

import jakarta.persistence.*;

@Entity
@Table(name = "satisfaction_level")
public class SatisfactionLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "satisfaction_level_id")
    private Long id;

    @Column(name = "level_name", nullable = false)
    private String levelName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

}
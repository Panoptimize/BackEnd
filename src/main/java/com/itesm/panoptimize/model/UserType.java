package com.itesm.panoptimize.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_type")
public class UserType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_type_id")
    private int id;

    @Column(name = "user_type_name", nullable = false)
    private String typeName;
}
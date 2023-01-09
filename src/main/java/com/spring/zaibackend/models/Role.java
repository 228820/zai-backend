package com.spring.zaibackend.models;


import javax.persistence.*;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private ERole role;

    public Role() {}
    public ERole getRole() {
        return role;
    }
}
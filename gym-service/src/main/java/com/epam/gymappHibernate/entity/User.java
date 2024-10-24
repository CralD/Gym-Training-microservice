package com.epam.gymappHibernate.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "USERS")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "FIRSTNAME", nullable = false)
    private String firstName;
    @Column(name = "LASTNAME", nullable = false)
    private String lastName;
    @Column(name = "USERNAME ", nullable = false)
    private String userName;
    @Column(name = "PASSWORD ", nullable = false)
    private String password;
    @Column(name = "IS_ACTIVE", nullable = false)
    private boolean isActive;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Trainee trainee;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Trainer trainer;

    public User() {

    }


}

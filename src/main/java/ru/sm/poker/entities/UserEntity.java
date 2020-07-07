package ru.sm.poker.entities;

import javax.persistence.*;

@Table(name = "users")
@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    private String name;

    private String password;

}

package com.spring.urlshortner.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a registered user with login credentials.
 */
@Getter
@Setter
@Entity
@Table(name = "Users")
public class User {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;
    private String username;
    private String password;

}

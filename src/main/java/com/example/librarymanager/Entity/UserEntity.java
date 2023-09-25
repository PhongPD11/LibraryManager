package com.example.librarymanager.Entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "user")
@Data
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //profile
    private Long uid;
    private String username;
    private String password;
    private String email;
    //role
    private Boolean isAdmin;
    //device
    private String fcm;
    //verify
    private Boolean isEnabled;
    private Long activeCode;
}

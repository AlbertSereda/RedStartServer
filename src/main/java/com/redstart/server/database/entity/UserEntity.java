package com.redstart.server.database.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users", schema = "public", catalog = "redstart")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @Column(name = "login")
    private String login;
    @Basic
    @Column(name = "password")
    private String password;
    @Basic
    @Column(name = "email")
    private String email;
    @Basic
    @CreationTimestamp
    @Column(name = "date_registration")
    private LocalDateTime dateRegistration;
    @Basic
    @Column(name = "active")
    private boolean active;
//    @OneToOne
//    @JoinColumn(name = "login")
//    private UserDataEntity UserDataEntity;
}

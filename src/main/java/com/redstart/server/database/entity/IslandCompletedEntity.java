package com.redstart.server.database.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "islands_completed", schema = "public", catalog = "redstart")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IslandCompletedEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "login_user")
    private String login;
    @Basic
    @Column(name = "island_id")
    private String island;

//    @OneToOne(optional = false)
//    private IslandEntity island;
}

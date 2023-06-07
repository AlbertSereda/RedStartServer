package com.redstart.server.database.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "levels_completed", schema = "public", catalog = "redstart")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LevelCompletedEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "count_complete")
    private int countComplete;
    @Basic
    @Column(name = "island_name")
    private String island;
    @Basic
    @Column(name = "login_user")
    private String login;
    @OneToOne(optional = false)
    private LevelEntity level;
}

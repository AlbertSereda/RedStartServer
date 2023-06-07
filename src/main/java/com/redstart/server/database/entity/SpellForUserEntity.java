package com.redstart.server.database.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "spells_for_users", schema = "public", catalog = "redstart")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpellForUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "cost_mana")
    private int costMana;
    @Basic
    @Column(name = "damage")
    private int damage;
    @Basic
    @Column(name = "duration_time")
    private int durationTime;
    @Basic
    @Column(name = "login_user")
    private String login;
    @Basic
    @Column(name = "level")
    private int level;
}

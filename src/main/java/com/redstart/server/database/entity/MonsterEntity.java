package com.redstart.server.database.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "monsters", schema = "public", catalog = "redstart")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonsterEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "damage")
    private int damage;
    @Basic
    @Column(name = "hp")
    private int hp;
    @Basic
    @Column(name = "boss")
    private boolean isBoss;
    @Basic
    @Column(name = "speed")
    private int speed;
}

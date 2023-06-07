package com.redstart.server.database.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "spells_default", schema = "public", catalog = "redstart")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpellDefaultEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
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
}

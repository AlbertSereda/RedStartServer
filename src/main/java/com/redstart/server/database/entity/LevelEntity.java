package com.redstart.server.database.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;

@Entity
@Table(name = "levels", schema = "public", catalog = "redstart")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LevelEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "level_number")
    private int levelNumber;
    @Basic
    @Column(name = "max_complete")
    private int maxComplete;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "monster_id", referencedColumnName = "id")
    private MonsterEntity monster;
}

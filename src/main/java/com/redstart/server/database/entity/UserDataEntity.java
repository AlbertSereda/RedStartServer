package com.redstart.server.database.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "users_data", schema = "public", catalog = "redstart")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDataEntity {
    @Id
    @Basic
    @Column(name = "login_user")
    private String loginUser;
    @Basic
    @Column(name = "level")
    private int level;
    @Basic
    @Column(name = "money")
    private int money;
    @Basic
    @Column(name = "experience")
    private int experience;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "login_user")
    @Fetch(value = FetchMode.SUBSELECT)
    private Collection<SpellForUserEntity> spellsForUser = new ArrayList<>();
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "login_user")
    @Fetch(value = FetchMode.SUBSELECT)
    private Collection<LevelCompletedEntity> levelsCompleted = new ArrayList<>();
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "login_user")
    @Fetch(value = FetchMode.SUBSELECT)
    private Collection<IslandCompletedEntity> islandsCompleted = new ArrayList<>();
}

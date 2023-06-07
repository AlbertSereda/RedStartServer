package com.redstart.server.database.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "islands", schema = "public", catalog = "redstart")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IslandEntity {
    @Id
    @Column(name = "id")
    private String name;

    @Basic
    @Column(name = "available")
    private boolean isAvailable;

    @Basic
    @Column(name = "island_number")
    private int islandNumber;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "spell_name", referencedColumnName = "name")
    private SpellDefaultEntity spell;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "island_id")
    @Fetch(value = FetchMode.SUBSELECT)
    private Collection<LevelEntity> levels = new ArrayList<>();
}

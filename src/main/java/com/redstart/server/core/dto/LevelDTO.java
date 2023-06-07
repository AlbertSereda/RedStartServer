package com.redstart.server.core.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.redstart.server.database.entity.LevelEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LevelDTO {
    @JsonIgnore
    private int id;
    private int levelNumber;
    private int maxCompleted;
    private MonsterDTO monster;

    public static List<LevelDTO> of(Collection<LevelEntity> levelsEntity) {
        return levelsEntity.stream()
                .map(LevelDTO::of)
                .toList();
    }

    public static LevelDTO of (LevelEntity levelEntity) {
        LevelDTO levelDTO = new LevelDTO();
        levelDTO.setId(levelEntity.getId());
        levelDTO.setLevelNumber(levelEntity.getLevelNumber());
        levelDTO.setMaxCompleted(levelEntity.getMaxComplete());

        MonsterDTO monsterDTO = MonsterDTO.of(levelEntity.getMonster());
        levelDTO.setMonster(monsterDTO);
        return levelDTO;
    }
}

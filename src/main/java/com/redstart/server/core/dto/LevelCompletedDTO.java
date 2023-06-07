package com.redstart.server.core.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.redstart.server.database.entity.LevelCompletedEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LevelCompletedDTO {
    @JsonIgnore
    private int id;
    private int levelNumber;
    private String island;
    private int countComplete;
    @JsonIgnore
    private LevelDTO level;
    public static LevelCompletedDTO of(LevelCompletedEntity levelCompletedEntity) {
        LevelCompletedDTO levelCompletedDTO = new LevelCompletedDTO();
        levelCompletedDTO.setId(levelCompletedEntity.getId());
        levelCompletedDTO.setLevelNumber(levelCompletedEntity.getLevel().getLevelNumber());
        levelCompletedDTO.setIsland(levelCompletedEntity.getIsland());
        levelCompletedDTO.setCountComplete(levelCompletedEntity.getCountComplete());

        LevelDTO levelDTO = LevelDTO.of(levelCompletedEntity.getLevel());
        levelCompletedDTO.setLevel(levelDTO);
        return levelCompletedDTO;
    }
}

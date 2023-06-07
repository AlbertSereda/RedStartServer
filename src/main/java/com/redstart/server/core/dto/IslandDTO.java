package com.redstart.server.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.redstart.server.database.entity.IslandEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IslandDTO {
    private String name;
    @JsonProperty("isAvailable")
    private boolean isAvailable;
    private List<LevelDTO> levels;
    private SpellDTO spell;
    private int islandNumber;

    public static List<IslandDTO> of(List<IslandEntity> allIslandsEntity) {
        return allIslandsEntity.stream()
                .map(islandEntity -> {
                    IslandDTO islandDTO = new IslandDTO();
                    islandDTO.setName(islandEntity.getName());
                    islandDTO.setAvailable(islandEntity.isAvailable());
                    islandDTO.setIslandNumber(islandEntity.getIslandNumber());

                    SpellDTO spellDTO = SpellDTO.of(islandEntity.getSpell());
                    islandDTO.setSpell(spellDTO);

                    List<LevelDTO> levelsDTO = LevelDTO.of(islandEntity.getLevels());
                    islandDTO.setLevels(levelsDTO);
                    return islandDTO;
                })
                .toList();
    }
}

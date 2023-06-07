package com.redstart.server.core.dto;

import com.redstart.server.database.entity.UserDataEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String login;
    private int level;
    private int money;
    private int experience;
    private List<SpellForUserDTO> spellsForUser;
    private List<LevelCompletedDTO> levelsCompleted;
    private List<IslandCompletedDTO> islandsCompleted;

    public static UserDTO of(UserDataEntity entity) {
        UserDTO dto = new UserDTO();

        dto.setLogin(entity.getLoginUser());
        dto.setLevel(entity.getLevel());
        dto.setMoney(entity.getMoney());
        dto.setExperience(entity.getExperience());

        dto.setSpellsForUser(entity.getSpellsForUser()
                .stream()
                .map(SpellForUserDTO::of)
                .collect(Collectors.toList()));

        dto.setLevelsCompleted(entity.getLevelsCompleted()
                .stream()
                .map(LevelCompletedDTO::of)
                .collect(Collectors.toList()));

        dto.setIslandsCompleted(entity.getIslandsCompleted()
                .stream()
                .map(IslandCompletedDTO::of)
                .collect(Collectors.toList()));

        return dto;
    }
}

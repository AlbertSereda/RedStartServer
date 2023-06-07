package com.redstart.server.core.dto;

import com.redstart.server.database.entity.UserDataEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaderDTO {
    private String login;
    private int level;

    public static LeaderDTO of(UserDataEntity userDataEntity) {
        LeaderDTO leaderDTO = new LeaderDTO();
        leaderDTO.setLogin(userDataEntity.getLoginUser());
        leaderDTO.setLevel(userDataEntity.getLevel());
        return leaderDTO;
    }
}

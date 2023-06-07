package com.redstart.server.database.service;

import com.redstart.server.core.dto.UserDTO;
import com.redstart.server.database.entity.UserDataEntity;

import java.util.List;

public interface UserDataService {
    UserDTO getUserdata(String login);

    void updateUserMoney(UserDTO userDTO);

    UserDTO createUserData(String login);

    void updateUserExperience(UserDTO userDTO, int earnedExperience);

    List<UserDataEntity> getUsersDataToLeaderboard();
}

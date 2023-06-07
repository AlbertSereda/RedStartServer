package com.redstart.server.database.service;

import com.redstart.server.core.dto.LevelCompletedDTO;
import com.redstart.server.core.dto.LevelDTO;
import com.redstart.server.core.dto.UserDTO;

public interface LevelCompletedService {
    LevelCompletedDTO saveCompletedLevel(UserDTO userDTO, String nameIsland, LevelDTO levelDTO);
}

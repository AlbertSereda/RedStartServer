package com.redstart.server.database.service;

import com.redstart.server.core.dto.IslandDTO;
import com.redstart.server.core.dto.LevelDTO;
import com.redstart.server.core.dto.UserDTO;
import com.redstart.server.database.entity.IslandEntity;

import java.util.List;
import java.util.Optional;

public interface IslandService {
    Optional<IslandEntity> findById(String id);

    List<IslandDTO> getAllIslands();

    LevelDTO getSelectedLevel(UserDTO user, String selectedIsland, int selectedLevel);
}

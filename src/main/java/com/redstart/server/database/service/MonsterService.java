package com.redstart.server.database.service;

import com.redstart.server.core.dto.LevelCompletedDTO;
import com.redstart.server.core.dto.LevelDTO;
import com.redstart.server.core.dto.MonsterDTO;

import java.util.List;

public interface MonsterService {

    MonsterDTO getMonsterForGameRoom(List<LevelCompletedDTO> levelsCompleted, LevelDTO selectedLevel, String nameSelectedIsland, int numberSelectedLevel);
}

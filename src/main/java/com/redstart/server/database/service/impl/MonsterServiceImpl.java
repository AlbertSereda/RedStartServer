package com.redstart.server.database.service.impl;

import com.redstart.server.core.dto.LevelCompletedDTO;
import com.redstart.server.core.dto.LevelDTO;
import com.redstart.server.core.dto.MonsterDTO;
import com.redstart.server.database.repository.MonsterRepository;
import com.redstart.server.database.service.MonsterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MonsterServiceImpl implements MonsterService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final MonsterRepository repository;

    public MonsterServiceImpl(MonsterRepository repository) {
        this.repository = repository;
    }

    @Override
    public MonsterDTO getMonsterForGameRoom(List<LevelCompletedDTO> levelsCompleted, LevelDTO selectedLevel, String nameSelectedIsland, int numberSelectedLevel) {
        Optional<LevelCompletedDTO> maybeCompletedLevel = levelsCompleted
                .stream()
                .filter(levelCompletedDTO -> levelCompletedDTO.getIsland().equals(nameSelectedIsland))
                .filter(levelCompletedDTO -> levelCompletedDTO.getLevelNumber() == numberSelectedLevel)
                .findFirst();

        if (maybeCompletedLevel.isPresent()) {
            log.info("boost monster");
            LevelCompletedDTO levelCompletedDTO = maybeCompletedLevel.get();
            int countCompletedLevel = levelCompletedDTO.getCountComplete();

            return boostMonster(selectedLevel, countCompletedLevel);
        } else {
            return selectedLevel.getMonster();
        }
    }

    private MonsterDTO boostMonster(LevelDTO selectedLevel, int countCompletedLevel) {
        MonsterDTO monster = selectedLevel.getMonster();
        monster.setHp(monster.getHp() + (countCompletedLevel * 100));
        monster.setSpeed(monster.getSpeed() - (countCompletedLevel * 250));
        monster.setDamage(monster.getDamage() + (countCompletedLevel * 2));
        return monster;
    }
}

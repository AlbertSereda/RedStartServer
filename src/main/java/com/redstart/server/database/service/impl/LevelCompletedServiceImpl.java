package com.redstart.server.database.service.impl;

import com.redstart.server.core.dto.LevelCompletedDTO;
import com.redstart.server.core.dto.LevelDTO;
import com.redstart.server.core.dto.UserDTO;
import com.redstart.server.database.entity.LevelCompletedEntity;
import com.redstart.server.database.entity.LevelEntity;
import com.redstart.server.database.repository.LevelCompletedRepository;
import com.redstart.server.database.repository.LevelRepository;
import com.redstart.server.database.service.LevelCompletedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class LevelCompletedServiceImpl implements LevelCompletedService {
    private static final Logger log = LoggerFactory.getLogger(LevelCompletedServiceImpl.class);
    private final LevelCompletedRepository levelCompletedRepository;
    private final LevelRepository levelRepository;

    public LevelCompletedServiceImpl(LevelCompletedRepository levelCompletedRepository,
                                     LevelRepository levelRepository) {
        this.levelCompletedRepository = levelCompletedRepository;
        this.levelRepository = levelRepository;
    }

    @Override
    @Transactional
    public LevelCompletedDTO saveCompletedLevel(UserDTO userDTO, String nameIsland, LevelDTO levelDTO) {
        Optional<LevelCompletedDTO> maybeCompletedLevel = userDTO.getLevelsCompleted()
                .stream()
                .filter(levelCompletedDTO -> levelCompletedDTO.getIsland().equals(nameIsland))
                .filter(levelCompletedDTO -> levelCompletedDTO.getLevelNumber() == levelDTO.getLevelNumber())
                .findFirst();

        if (maybeCompletedLevel.isPresent()) {
            //уровень завершенный уже есть
            LevelCompletedDTO levelCompletedDTO = maybeCompletedLevel.get();
            Optional<LevelCompletedEntity> maybeLevelCompleted = levelCompletedRepository.findById(levelCompletedDTO.getId());
            if (maybeLevelCompleted.isPresent()) {
                LevelCompletedEntity levelCompletedEntity = maybeLevelCompleted.get();
                if (levelCompletedEntity.getCountComplete() < 3) {
                    levelCompletedEntity.setCountComplete(levelCompletedEntity.getCountComplete() + 1);
                    levelCompletedRepository.save(levelCompletedEntity);
                    levelCompletedDTO.setCountComplete(levelCompletedDTO.getCountComplete() + 1);
                    log.info("Update completed level {}", levelCompletedEntity);
                }
            }
            return levelCompletedDTO;
        } else {
            LevelCompletedEntity levelCompletedEntity = new LevelCompletedEntity();
            levelCompletedEntity.setCountComplete(1);
            levelCompletedEntity.setIsland(nameIsland);
            levelCompletedEntity.setLogin(userDTO.getLogin());

            Optional<LevelEntity> maybeLevelEntity = levelRepository.findById(levelDTO.getId());
            if (maybeLevelEntity.isPresent()) {
                LevelEntity levelEntity = maybeLevelEntity.get();
                levelCompletedEntity.setLevel(levelEntity);
            }

            levelCompletedRepository.save(levelCompletedEntity);
            log.info("Saved new completed level {}", levelCompletedEntity);
            return LevelCompletedDTO.of(levelCompletedEntity);
        }
    }
}

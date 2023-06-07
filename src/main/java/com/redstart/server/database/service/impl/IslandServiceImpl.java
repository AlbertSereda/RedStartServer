package com.redstart.server.database.service.impl;

import com.redstart.server.core.dto.*;
import com.redstart.server.database.entity.IslandEntity;
import com.redstart.server.database.repository.IslandRepository;
import com.redstart.server.database.service.IslandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.redstart.server.core.socket.message.responsedata.ErrorResponse.*;

@Service
public class IslandServiceImpl implements IslandService {
    private static final Logger log = LoggerFactory.getLogger(IslandServiceImpl.class);
    private final IslandRepository repository;
    private List<IslandDTO> allIslandsCash;

    public IslandServiceImpl(IslandRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public Optional<IslandEntity> findById(String id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public List<IslandDTO> getAllIslands() {
        if (allIslandsCash == null) {
            List<IslandEntity> allIslandsEntity = repository.findAll();
            allIslandsCash = IslandDTO.of(allIslandsEntity);
        }
        return allIslandsCash;
    }

    @Override
    public LevelDTO getSelectedLevel(UserDTO user, String selectedIsland, int selectedLevel) {
        //todo может острова дополнительные хранить с индексом -1, -2 ... и не учитывать их при запуске
        //TODO так же и в коллекции игрока не учитывать

        List<IslandDTO> allIslands = getAllIslands();

        Optional<IslandDTO> maybeSelectedIslandDTO = allIslands.stream()
                .filter(islandDTO -> islandDTO.getName().equals(selectedIsland))
                .findFirst();

        if (maybeSelectedIslandDTO.isPresent()) {
            IslandDTO selectedIslandDTO = maybeSelectedIslandDTO.get();

            if (selectedIslandDTO.isAvailable()) {
                //получаем какие острова можно запустить игроку:
                //получаем последний пройденный остров
                //если его нет, то берем номер первого острова
                //если последний пройденный = последнему острову в игре то берем его номер
                //если последний пройденный в промежутке то берем следующий номер
                //острова которые игрок может проходить это номера островов меньше или равные найденному.


                int numberAvailableIsland = getNumberAvailableIsland(user, allIslands);

                if (selectedIslandDTO.getIslandNumber() <= numberAvailableIsland) {
                    //Все хорошо. Сейчас проверяем уровень на этом острове
                    List<LevelCompletedDTO> completedLevelsOnIsland = user.getLevelsCompleted()
                            .stream()
                            //оставляем завершенные уровни на этом острове
                            .filter(levelCompleted -> levelCompleted.getIsland().equals(selectedIslandDTO.getName()))
                            .toList();

                    List<LevelDTO> allLevelsOnIsland = selectedIslandDTO.getLevels();

                    int numberAvailableLevel = getNumberAvailableLevel(completedLevelsOnIsland, allLevelsOnIsland);

                    if (selectedLevel <= numberAvailableLevel) {
                        return allLevelsOnIsland.get(selectedLevel - 1);
//                        Ниже код для проверки, пройден ли уровень максимальное количество раз
//                        //проверяем что клиент уже проходил этот уровень
//                        Optional<LevelCompletedDTO> maybeCompletedLevel = completedLevelsOnIsland
//                                .stream()
//                                .filter(levelCompletedDTO -> levelCompletedDTO.getLevelNumber() == selectedLevel)
//                                .findFirst();
//
//                        if (maybeCompletedLevel.isPresent()) {
//                            //Клиент выбрал уровень, который проходил
//                            //проверяем сколько раз он его проходил
//                            LevelCompletedDTO selectCompletedLevel = maybeCompletedLevel.get();
//                            LevelDTO levelDTO = selectCompletedLevel.getLevel();
//                            if (selectCompletedLevel.getCountComplete() < levelDTO.getMaxCompleted()) {
//                                //возвращаем уровень выбранный
//                                return levelDTO;
//                            } else {
//                                log.error("Клиент прошел уровень максимальное количество раз. " +
//                                                "login : {}. Selected island : {}. Selected level : {}",
//                                        user.getLogin(), selectedIsland, selectedLevel);
//                                throw new IllegalStateException(MAX_COUNT_LEVEL_COMPLETED.getErrorMessage());
//                            }
//                        } else {
//                            return allLevelsOnIsland.get(selectedLevel - 1);
//                        }

                    } else {
                        log.error("Клиент выбрал не доступный для него уровень на острове. " +
                                        "login : {}. Selected island : {}. Selected level : {}",
                                user.getLogin(), selectedIsland, selectedLevel);
                        throw new IllegalStateException(INACCESSIBLE_LEVEL.getErrorMessage());
                    }
                } else {
                    log.error("Клиент выбрал не доступный для него остров. " +
                                    "login : {}. Selected island : {}. Selected level : {}",
                            user.getLogin(), selectedIsland, selectedLevel);
                    throw new IllegalStateException(INACCESSIBLE_ISLAND.getErrorMessage());
                }
            } else {
                log.error("Выбранный остров не доступен на данный момент. " +
                                "login : {}. Selected island : {}. Selected level : {}",
                        user.getLogin(), selectedIsland, selectedLevel);
                throw new IllegalStateException(INACCESSIBLE_ISLAND.getErrorMessage());
            }
        } else {
            log.error("Такого острова не существует. " +
                            "login : {}. Selected island : {}. Selected level : {}",
                    user.getLogin(), selectedIsland, selectedLevel);
            throw new IllegalStateException(ISLAND_NOT_FOUND.getErrorMessage());
        }
    }

    private int getNumberAvailableIsland(UserDTO user, List<IslandDTO> allIslands) {
        //получаем номер доступного острова
        List<IslandCompletedDTO> islandsCompleted = user.getIslandsCompleted();
        int numberAvailableIsland = 1;
        if (islandsCompleted.size() != 0) {
            if (islandsCompleted.size() == allIslands.size()) {
                numberAvailableIsland = allIslands.size();
            } else {
                numberAvailableIsland = islandsCompleted.size() + 1;
            }
        }
        return numberAvailableIsland;
    }

    private int getNumberAvailableLevel(List<LevelCompletedDTO> completedLevelsOnIsland, List<LevelDTO> allLevelsOnIsland) {
        //получаем номер доступного уровня

        int numberAvailableLevel = allLevelsOnIsland.size() - 1;
        if (completedLevelsOnIsland.size() != 0) {
            //считаем общее количество убийств не босса
            int countCompletedMonster = completedLevelsOnIsland
                    .stream()
                    .filter(levelCompletedDTO -> !levelCompletedDTO.getLevel().getMonster().isBoss())
                    .mapToInt(LevelCompletedDTO::getCountComplete)
                    .sum();

            //считаем общее количество убийств босса
            int countCompletedBoss = completedLevelsOnIsland
                    .stream()
                    .filter(levelCompletedDTO -> levelCompletedDTO.getLevel().getMonster().isBoss())
                    .mapToInt(LevelCompletedDTO::getCountComplete)
                    .sum();

            int i = countCompletedMonster - (countCompletedBoss * (allLevelsOnIsland.size() - 1));

            if (i >= (allLevelsOnIsland.size() - 1)) {
                numberAvailableLevel = allLevelsOnIsland.size();
            }
        }
        return numberAvailableLevel;
    }
}

package com.redstart.server.core.socket.message.processor.user;

import com.redstart.server.core.dto.LevelDTO;
import com.redstart.server.core.dto.UserDTO;
import com.redstart.server.core.gamemechanics.GameRoom;
import com.redstart.server.core.gamemechanics.GameState;
import com.redstart.server.core.socket.SocketClient;
import com.redstart.server.core.socket.message.SocketEventType;
import com.redstart.server.core.socket.message.processor.AuthSocketEventProcessor;
import com.redstart.server.core.socket.message.requestdata.adventure.GameOverRequestData;
import com.redstart.server.core.socket.message.responsedata.adventure.GameOverResponseData;
import com.redstart.server.core.socket.repository.GameRoomRepository;
import com.redstart.server.database.service.LevelCompletedService;
import com.redstart.server.database.service.SpellForUserService;
import com.redstart.server.database.service.UserDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class GameOverEventProcessor extends AuthSocketEventProcessor<GameOverRequestData, GameOverResponseData> {
    private static final Logger log = LoggerFactory.getLogger(GameOverEventProcessor.class);
    private final GameRoomRepository gameRoomRepository;
    private final LevelCompletedService levelCompletedService;
    private final SpellForUserService spellForUserService;
    private final UserDataService userDataService;

    public GameOverEventProcessor(GameRoomRepository gameRoomRepository,
                                  LevelCompletedService levelCompletedService,
                                  SpellForUserService spellForUserService,
                                  UserDataService userDataService) {
        this.gameRoomRepository = gameRoomRepository;
        this.levelCompletedService = levelCompletedService;
        this.spellForUserService = spellForUserService;
        this.userDataService = userDataService;
    }

    @Override
    public SocketEventType getEventType() {
        return SocketEventType.ADVENTURE_GAME_OVER;
    }

    @Override
    protected GameOverResponseData action(GameOverRequestData data, SocketClient socketClient) {
        GameRoom gameRoom = data.getGameRoom();
        GameState gameState = gameRoom.getAdventureData().getGameState();
        UserDTO userDTO = gameRoom.getUserDTO();

        int earnedMoney = 0;
        int earnedExperience = 0;

        if (gameState == GameState.WIN || gameState == GameState.LOSE) {
            earnedMoney = gameRoom.getPlayer().getEarnedMoney();
            earnedExperience = getEarnedExperience(gameRoom.getStartGame(), gameRoom.getEndGame(), gameRoom.getEarnedExperience());

            if (gameState == GameState.WIN) {
                //TODO если выиграл, то надо ждать, будет ли игрок смотреть рекламу. Добавлять в какой то список,
                // и ждать что нажал клиент, выйти или смотреть рекламу. Если выйти то придет ивент что клиент не смотрит рекламу,
                // значит удаляем из списка. А если смотрит то ждем ивента от сервера юнити что посмотрел.
                // После этого начисляем доп монеты и отправляет клиенту обновление по монетам и удаляем у себя из списка.
                // Как пришел ивент от юнинти что посмотрел рекламу, заодно удаляем другие объекты которые ждут рекламу больше 15 минут
                // Ивент watchAd дата boolean isWatch = true/false
                // если игрок смотрит рекламу то начислляем сразу бабки, а проверку делаем потом. Если не было в течении часа, то вычитаем деньги



                String nameIsland = gameRoom.getNameIsland();
                LevelDTO levelDTO = gameRoom.getLevelDTO();

                levelCompletedService.saveCompletedLevel(userDTO, nameIsland, levelDTO);

                //если босс, то работаем с заклинанием
                if (levelDTO.getMonster().isBoss()) {
                    spellForUserService.updateUserSpellFromBoss(userDTO, nameIsland);
                }

                //сохраняем заработанные монетки
                if (earnedMoney > 0) {
                    earnedMoney *= 1.5;
                    userDTO.setMoney(userDTO.getMoney() + earnedMoney);
                    userDataService.updateUserMoney(userDTO);
                }

                //начисляем опыт с бонусом
                earnedExperience *= 1.5;
                userDataService.updateUserExperience(userDTO, earnedExperience);

            } else {
                //todo Смотрим воскрешался он уже или нет
                // перед тем как начислять опыт и монетки, ждем будет ли клиент смотреть рекламу за воскрешение.
                // для этого комнату передаем в другой список
                // Если пришел ивент что клиент запустил рекламу, то переносим комнату в другой список, записывать время когда занесли в список
                // как только пришел ивент чот игрок посмотрел рекламу, надо воссоздать комнату
                // в комнате делать флаг что клиент воскрешался уже
                // Если пришел ивент что не будет смотреть рекламу, то начисляем монетки и опыт заработанный
                // Ивент resurrectForAd дата boolean isResurrect = true/false
                // воскрешение делаем сразу, а если не было ответа от сервера в течении часа то все заработанное вычитаем.

                //начисляем опыт без бонуса
                userDataService.updateUserExperience(userDTO, earnedExperience);

                //сохраняем заработанные монетки
                if (earnedMoney > 0) {
                    userDTO.setMoney(userDTO.getMoney() + earnedMoney);
                    userDataService.updateUserMoney(userDTO);
                }
            }
        }

        gameRoomRepository.removeGameRoom(socketClient);
        log.info("Game room for {} removed. Count active game room: {}", userDTO.getLogin(), gameRoomRepository.getGameRooms().size());
        return GameOverResponseData.of(earnedMoney, earnedExperience);
    }

    private int getEarnedExperience(LocalDateTime startGame, LocalDateTime endGame, int earnedExperience) {
        earnedExperience += ChronoUnit.SECONDS.between(startGame, endGame);
        return earnedExperience;
    }
}

package com.redstart.server.core.socket.message.processor.user;

import com.redstart.server.core.dto.*;
import com.redstart.server.core.gamemechanics.GameLogic;
import com.redstart.server.core.gamemechanics.GameRoom;
import com.redstart.server.core.gamemechanics.spells.interfaces.ISpell;
import com.redstart.server.core.socket.SocketClient;
import com.redstart.server.core.socket.message.SocketEventType;
import com.redstart.server.core.socket.message.processor.AuthSocketEventProcessor;
import com.redstart.server.core.socket.message.requestdata.user.CreateRoomRequestData;
import com.redstart.server.core.socket.message.responsedata.user.CreateRoomResponseData;
import com.redstart.server.core.socket.repository.GameRoomRepository;
import com.redstart.server.database.service.IslandService;
import com.redstart.server.database.service.MonsterService;
import com.redstart.server.database.service.SpellForUserService;
import com.redstart.server.database.service.UserDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CreateRoomEventProcessor extends AuthSocketEventProcessor<CreateRoomRequestData, CreateRoomResponseData> {
    private static final Logger log = LoggerFactory.getLogger(CreateRoomEventProcessor.class);
    private final GameLogic gameLogic;
    private final GameRoomRepository gameRoomRepository;
    private final UserDataService userDataService;
    private final IslandService islandService;
    private final SpellForUserService spellForUserService;
    private final MonsterService monsterService;

    public CreateRoomEventProcessor(GameLogic gameLogic,
                                    GameRoomRepository gameRoomRepository,
                                    UserDataService userDataService,
                                    IslandService islandService,
                                    SpellForUserService spellForUserService,
                                    MonsterService monsterService) {
        this.gameLogic = gameLogic;
        this.gameRoomRepository = gameRoomRepository;
        this.userDataService = userDataService;
        this.islandService = islandService;
        this.spellForUserService = spellForUserService;
        this.monsterService = monsterService;
    }

    @Override
    protected CreateRoomResponseData action(CreateRoomRequestData data, SocketClient socketClient) {
        gameRoomRepository.getGameRoom(socketClient);

        UserDTO user = userDataService.getUserdata(socketClient.getLogin());

        List<SpellForUserDTO> selectedSpellsDTO = spellForUserService.getSelectedSpells(user, data.getUserSelectedSpells());
        log.info("User {} selected spells: {}", user.getLogin(), selectedSpellsDTO);
        List<ISpell> selectedSpells = SpellForUserDTO.convert(selectedSpellsDTO);

        String selectedIsland = data.getIslandName();
        int selectedLevel = data.getLevelNumber();

        LevelDTO level = islandService.getSelectedLevel(user, selectedIsland, selectedLevel);
        log.info("User {} selected level {} on island {}", user.getLogin(), level.getLevelNumber(), selectedIsland);

        //Буст монстра от количества его убийств
        MonsterDTO monsterDTO = monsterService.getMonsterForGameRoom(user.getLevelsCompleted(),
                level,
                selectedIsland,
                selectedLevel);

        GameRoom gameRoom = new GameRoom(gameLogic,
                user.getLogin(),
                selectedSpells,
                monsterDTO,
                level,
                selectedIsland,
                user);

        gameRoomRepository.addGameRoom(socketClient, gameRoom);
        return CreateRoomResponseData.of(gameRoom);
    }

    @Override
    public SocketEventType getEventType() {
        return SocketEventType.USER_CREATE_ROOM;
    }
}

package com.redstart.server.core.message.processor.user;

import com.redstart.server.core.SocketClient;
import com.redstart.server.core.gamemechanics.GameLogic;
import com.redstart.server.core.gamemechanics.GameRoom;
import com.redstart.server.core.message.SocketEventType;
import com.redstart.server.core.message.processor.AuthSocketEventProcessor;
import com.redstart.server.core.message.requestdata.user.CreateRoomRequestData;
import com.redstart.server.core.message.responsedata.adventure.AdventureResponseData;
import com.redstart.server.core.repository.GameRoomRepository;
import org.springframework.stereotype.Component;

@Component
public class CreateRoomEventProcessor extends AuthSocketEventProcessor<CreateRoomRequestData, AdventureResponseData> {
    private final GameLogic gameLogic;
    private final GameRoomRepository gameRoomRepository;

    public CreateRoomEventProcessor(GameLogic gameLogic,
                                    GameRoomRepository gameRoomRepository) {
        this.gameLogic = gameLogic;
        this.gameRoomRepository = gameRoomRepository;
    }

    @Override
    protected AdventureResponseData action(CreateRoomRequestData data, SocketClient socketClient) {
        GameRoom gameRoom = new GameRoom(gameLogic);
        gameRoom.getMonster().setNewTimeCreation();

        gameRoom.getPlayer().getSpawnedBlocks().clear();
        gameRoomRepository.addGameRoom(socketClient, gameRoom);

        return AdventureResponseData.of(gameRoom);
    }

    @Override
    public SocketEventType getEventType() {
        return SocketEventType.USER_CREATE_ROOM;
    }
}

package com.redstart.server.core.socket.message.processor.user;

import com.redstart.server.core.socket.SocketClient;
import com.redstart.server.core.gamemechanics.GameLogic;
import com.redstart.server.core.gamemechanics.GameRoom;
import com.redstart.server.core.socket.message.SocketEventType;
import com.redstart.server.core.socket.message.processor.AuthSocketEventProcessor;
import com.redstart.server.core.socket.message.requestdata.user.CreateRoomRequestData;
import com.redstart.server.core.socket.message.responsedata.adventure.AdventureResponseData;
import com.redstart.server.core.socket.repository.GameRoomRepository;
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

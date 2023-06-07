package com.redstart.server.core.socket.message.processor.adventure;

import com.redstart.server.core.gamemechanics.GameRoom;
import com.redstart.server.core.gamemechanics.GameState;
import com.redstart.server.core.socket.message.SocketEventType;
import com.redstart.server.core.socket.message.requestdata.user.CreateRoomSuccessRequestData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CreateRoomSuccessEventProcessor extends AdventureSocketEventProcessor<CreateRoomSuccessRequestData> {
    private static final Logger log = LoggerFactory.getLogger(CreateRoomSuccessEventProcessor.class);
    @Override
    public SocketEventType getEventType() {
        return SocketEventType.USER_CREATED_ROOM_SUCCESS;
    }

    @Override
    protected void processEvent(CreateRoomSuccessRequestData data, GameRoom gameRoom) {
        gameRoom.getPlayer().getSpawnedBlocks().clear();
        gameRoom.getMonster().setNewTimeCreation();
        gameRoom.setStartGame(LocalDateTime.now());
        gameRoom.getAdventureData().setGameState(GameState.RESUME);
    }
}

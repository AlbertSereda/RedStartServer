package com.redstart.server.core.socket.message.processor.adventure;

import com.redstart.server.core.gamemechanics.GameRoom;
import com.redstart.server.core.socket.message.SocketEventType;
import com.redstart.server.core.socket.message.requestdata.adventure.GameOverRequestData;
import com.redstart.server.core.socket.repository.GameRoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GameOverEventProcessor extends AdventureSocketEventProcessor<GameOverRequestData> {
    private static final Logger log = LoggerFactory.getLogger(GameOverEventProcessor.class);
    private final GameRoomRepository gameRoomRepository;

    public GameOverEventProcessor(GameRoomRepository gameRoomRepository) {
        this.gameRoomRepository = gameRoomRepository;
    }

    @Override
    public SocketEventType getEventType() {
        return SocketEventType.ADVENTURE_GAME_OVER;
    }

    @Override
    protected void processEvent(GameOverRequestData data, GameRoom gameRoom) {
        gameRoomRepository.removeGameRoom(data.getSocketClient());
        log.info("Count game room object in map: {}", gameRoomRepository.getGameRooms().size());
    }
}

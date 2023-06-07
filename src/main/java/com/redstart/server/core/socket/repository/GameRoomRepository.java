package com.redstart.server.core.socket.repository;

import com.redstart.server.core.socket.SocketClient;
import com.redstart.server.core.gamemechanics.GameRoom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class GameRoomRepository {
    private static final Logger log = LoggerFactory.getLogger(GameRoomRepository.class);

    private final Map<SocketClient, GameRoom> gameRooms;

    public GameRoomRepository(@Qualifier("gameRoomRepositoryMap") Map<SocketClient, GameRoom> gameRooms) {
        this.gameRooms = gameRooms;
    }

    public void addGameRoom(SocketClient socketClient, GameRoom gameRoom) {
        gameRooms.put(socketClient, gameRoom);
    }

    public Map<SocketClient, GameRoom> getGameRooms() {
        return gameRooms;
    }

    public Optional<GameRoom> getGameRoom(SocketClient socketClient) {
        return Optional.ofNullable(gameRooms.get(socketClient));
    }

    public void removeGameRoom(SocketClient socketClient) {
        GameRoom removeGameRoom = gameRooms.remove(socketClient);
        if (removeGameRoom != null) {
            log.info("Removed game room {}", removeGameRoom.getPlayer().getName());
        } else {
            log.error("Failed remove game room {} ", socketClient.getLogin());
        }
    }
}

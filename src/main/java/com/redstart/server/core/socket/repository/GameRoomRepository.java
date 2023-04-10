package com.redstart.server.core.socket.repository;

import com.redstart.server.core.socket.SocketClient;
import com.redstart.server.core.gamemechanics.GameRoom;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class GameRoomRepository {

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
        gameRooms.remove(socketClient);
    }
}

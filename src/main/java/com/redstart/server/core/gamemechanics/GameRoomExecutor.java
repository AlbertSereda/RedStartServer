package com.redstart.server.core.gamemechanics;

import com.redstart.server.core.SocketClient;
import com.redstart.server.core.SocketHandler;
import com.redstart.server.core.message.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class GameRoomExecutor {

    private static final Logger log = LoggerFactory.getLogger(GameRoomExecutor.class);
    private final MessageHandler messageHandler;
    private final Map<SocketClient, GameRoom> gameRooms;

    private final ExecutorService executorService;

    private SocketHandler socketHandler;

    private GameLogic gameLogic;

    public GameRoomExecutor(MessageHandler messageHandler,
                            @Value("${gameRoomExecutor.countThread}") int nThreads,
                            GameLogic gameLogic) {
        this.messageHandler = messageHandler;
        this.gameLogic = gameLogic;
        gameRooms = new ConcurrentHashMap<>();
        executorService = Executors.newFixedThreadPool(nThreads);
    }

    public void createGameRoom(SocketClient socketClient) {
        executorService.execute(() -> {
            GameRoom gameRoom = new GameRoom(gameLogic);
            gameRoom.getMonster().setNewTimeCreation();

//            byte[] message = messageHandler.objectToJson(gameRoom.getAdventureData());
//            socketHandler.writeToBuffer(socketClient, message);
            socketHandler.addToQueueObject(socketClient, gameRoom);

            gameRoom.getPlayer().getSpawnedBlocks().clear();
            gameRooms.put(socketClient, gameRoom);

        });
    }

    public void removeGameRoom(SocketClient socketClient) {
        executorService.execute(() -> {
            gameRooms.remove(socketClient);
            log.info("Count game room object in map - " + gameRooms.size());
        });
    }

    public void setSocketHandler(SocketHandler socketHandler) {
        this.socketHandler = socketHandler;
    }

    public void setGameLogic(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
    }

    public Map<SocketClient, GameRoom> getGameRooms() {
        return gameRooms;
    }
}

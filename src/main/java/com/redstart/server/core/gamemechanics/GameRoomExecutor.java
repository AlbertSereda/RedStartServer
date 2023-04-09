package com.redstart.server.core.gamemechanics;

import com.redstart.server.core.SocketClient;
import com.redstart.server.core.SocketHandler;
import com.redstart.server.core.message.JsonMessageConverter;
import com.redstart.server.core.repository.GameRoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class GameRoomExecutor {

    private static final Logger log = LoggerFactory.getLogger(GameRoomExecutor.class);
    private final JsonMessageConverter jsonMessageConverter;
    //private final Map<SocketClient, GameRoom> gameRooms;

    private final ExecutorService executorService;

    private SocketHandler socketHandler;

    private GameLogic gameLogic;

    private final GameRoomRepository gameRoomRepository;

    public GameRoomExecutor(JsonMessageConverter jsonMessageConverter,
                            @Value("${gameRoomExecutor.countThread}") int nThreads,
                            GameLogic gameLogic, GameRoomRepository gameRoomRepository) {
        this.jsonMessageConverter = jsonMessageConverter;
        this.gameLogic = gameLogic;
        this.gameRoomRepository = gameRoomRepository;
        //gameRooms = new ConcurrentHashMap<>();
        executorService = Executors.newFixedThreadPool(nThreads);
    }

//    public void createGameRoom(SocketClient socketClient) {
//        executorService.execute(() -> {
//            GameRoom gameRoom = new GameRoom(gameLogic);
//            gameRoom.getMonster().setNewTimeCreation();
//
////            byte[] message = messageHandler.objectToJson(gameRoom.getAdventureData());
////            socketHandler.writeToBuffer(socketClient, message);
//            socketHandler.addToWriteObject(socketClient, gameRoom);
//
//            gameRoom.getPlayer().getSpawnedBlocks().clear();
//            //gameRooms.put(socketClient, gameRoom);
//            gameRoomRepository.addGameRoom(socketClient, gameRoom);
//
//        });
//    }

    public void removeGameRoom(SocketClient socketClient) {
        executorService.execute(() -> {
            //gameRooms.remove(socketClient);
            gameRoomRepository.removeGameRoom(socketClient);
            log.info("Count game room object in map - " + gameRoomRepository.getGameRooms().size());
        });
    }

    public void setSocketHandler(SocketHandler socketHandler) {
        this.socketHandler = socketHandler;
    }

    public void setGameLogic(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
    }

//    public Map<SocketClient, GameRoom> getGameRooms() {
//        return gameRooms;
//    }
}

package com.redstart.server.core.gamemechanics;

import com.redstart.server.core.SocketClient;
import com.redstart.server.core.SocketHandler;
import com.redstart.server.core.message.MessageHandler;
import com.redstart.server.core.message.SocketMessage;
import com.redstart.server.core.message.processor.ISocketEventProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;

@Component
public class GameLogicExecutor {

    private static final Logger log = LoggerFactory.getLogger(GameLogicExecutor.class);
    private final MessageHandler messageHandler;
    private GameLogic gameLogic;
    private final ExecutorService executorService;
    private SocketHandler socketHandler;

    private final GameRoomExecutor gameRoomExecutor;
    private final Map<String, ISocketEventProcessor> eventProcessorMap;

    private final Map<SocketClient, GameRoom> gameRooms;

    public GameLogicExecutor(MessageHandler messageHandler,
                             GameRoomExecutor gameRoomExecutor,
                             @Value("${gameLogicExecutor.countThread}") int nThreads,
                             GameLogic gameLogic,
                             Set<ISocketEventProcessor> eventProcessorMap) {
        this.messageHandler = messageHandler;
        this.gameRoomExecutor = gameRoomExecutor;
        gameRooms = gameRoomExecutor.getGameRooms();
        executorService = Executors.newFixedThreadPool(nThreads);
        this.gameLogic = gameLogic;

        this.eventProcessorMap = eventProcessorMap.stream()
                .collect(Collectors
                        .toMap(ISocketEventProcessor::getEventType,
                                socketEventProcessor -> socketEventProcessor));
    }

    public void addTasksToExecute(SocketClient socketClient, String message) {
        executorService.execute(() -> {
            String[] messageSplit = message.split("\n");
            String lastString = "";

            for (int i = 0; i < messageSplit.length; i++) {

                if (!lastString.equals(messageSplit[i])) {
                    executeMove(socketClient, Move.PLAYER, messageSplit[i]);
                    lastString = messageSplit[i];
                }
            }
        });
    }

    public void executeMove(SocketClient socketClient, Move whoMove, String message) {
        executorService.execute(() -> {
            SocketChannel socketChannel = socketClient.getSocketChannel();
            if (!socketChannel.isConnected()) {
                return;
            }
            GameRoom gameRoom = gameRooms.get(socketClient);
            if (gameRoom == null) {
                log.warn("Game Room = null for socketChannel - {}, count game room = {}", socketChannel, gameRooms.size());
                return;
            }

            if (gameRoom.getIsGameOver().get()) {
                return;
            }
            Lock lock = gameRoom.getLock();
            lock.lock();
            try {
                switch (whoMove) {
                    case PLAYER:
                        SocketMessage socketMessage = messageHandler.jsonToSocketMessage(message);

                        Optional<ISocketEventProcessor> maybeEventProcessor = getEventProcessor(socketMessage);
                        ISocketEventProcessor eventProcessor = maybeEventProcessor
                                .orElseThrow(() -> new IllegalStateException("Not found ISocketEventProcessor for - " + socketMessage.getGameEvent()));

                        eventProcessor.process(socketMessage, gameRoom);
                        //TODO event.processEvent(socketMessage, gameRoom);
                        break;
                    case MONSTER:
                        gameRoom.getMonster().getMonsterMoveLogic().monsterMove();
                        break;
                }
                socketHandler.addToQueueObject(socketClient, gameRoom);
            } finally {
                lock.unlock();
            }
        });
    }

    private Optional<ISocketEventProcessor> getEventProcessor(SocketMessage socketMessage) throws
            IllegalStateException {
        ISocketEventProcessor maybeEventProcessor = eventProcessorMap.get(socketMessage.getGameEvent());
        return Optional.of(maybeEventProcessor);
    }

    public void executeGameOver(SocketClient socketClient, GameRoom gameRoom) {
        executorService.execute(() -> {
            socketHandler.addToQueueObject(socketClient, gameRoom);
            gameRoomExecutor.removeGameRoom(socketClient);
        });
    }

    public void executeMove(SocketClient socketClient, Move whoMove) {
        executeMove(socketClient, whoMove, "");
    }

//    public void addToSendObject(SocketClient socketClient, GameRoom gameRoom) {
//        byte[] sendMessage = messageHandler.objectToJson(gameRoom.getAdventureData());
//        gameRoom.getPlayer().getBlastedBlocks().clear();
//        gameRoom.getPlayer().getSpawnedBlocks().clear();
//        socketHandler.writeToBuffer(socketClient, sendMessage);
//    }

    public void setSocketHandler(SocketHandler socketHandler) {
        this.socketHandler = socketHandler;
    }

    public void setGameLogic(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
    }
}

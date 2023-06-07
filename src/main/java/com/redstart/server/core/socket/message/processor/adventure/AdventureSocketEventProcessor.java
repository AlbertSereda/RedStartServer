package com.redstart.server.core.socket.message.processor.adventure;

import com.redstart.server.core.gamemechanics.GameRoom;
import com.redstart.server.core.socket.SocketClient;
import com.redstart.server.core.socket.message.processor.AuthSocketEventProcessor;
import com.redstart.server.core.socket.message.processor.user.GameOverEventProcessor;
import com.redstart.server.core.socket.message.requestdata.ISocketMessageRequestData;
import com.redstart.server.core.socket.message.responsedata.adventure.AdventureResponseData;
import com.redstart.server.core.socket.repository.GameRoomRepository;
import com.redstart.server.exception.GameRoomNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.locks.Lock;

public abstract class AdventureSocketEventProcessor<Q extends ISocketMessageRequestData>
        extends AuthSocketEventProcessor<Q, AdventureResponseData> {

    private static final Logger log = LoggerFactory.getLogger(AdventureSocketEventProcessor.class);

    private GameRoomRepository gameRoomRepository;

    @Override
    public AdventureResponseData action(Q data, SocketClient socketClient) {

        //todo убрать экспешен при отсутствии комнаты
        GameRoom gameRoom = gameRoomRepository.getGameRoom(socketClient)
                .orElseThrow(() -> new GameRoomNotFoundException("GameRoom not found for " + socketClient.getLogin()));

        if (gameRoom.getIsGameOver().get()) {
            log.info("The action cannot be performed for {} {}", socketClient.getLogin(), this.getClass().getName());
        }

        AdventureResponseData responseData;

        Lock lock = gameRoom.getLock();
        lock.lock();
        try {
            processEvent(data, gameRoom);
        } finally {
            responseData = AdventureResponseData.of(gameRoom);
            gameRoom.getPlayer().getBlastedBlocks().clear();
            gameRoom.getPlayer().getSpawnedBlocks().clear();
            lock.unlock();
        }
        return responseData;
    }

    protected abstract void processEvent(Q data, GameRoom gameRoom);

    @Autowired
    public void setGameRoomRepository(GameRoomRepository gameRoomRepository) {
        this.gameRoomRepository = gameRoomRepository;
    }
}

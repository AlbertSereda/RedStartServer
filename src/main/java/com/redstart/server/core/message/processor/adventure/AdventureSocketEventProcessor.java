package com.redstart.server.core.message.processor.adventure;

import com.redstart.server.core.SocketClient;
import com.redstart.server.core.gamemechanics.GameRoom;
import com.redstart.server.core.message.processor.AuthSocketEventProcessor;
import com.redstart.server.core.message.requestdata.ISocketMessageRequestData;
import com.redstart.server.core.message.responsedata.adventure.AdventureResponseData;
import com.redstart.server.core.repository.GameRoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.channels.SocketChannel;
import java.util.concurrent.locks.Lock;

public abstract class AdventureSocketEventProcessor<Q extends ISocketMessageRequestData>
        extends AuthSocketEventProcessor<Q, AdventureResponseData> {

    private static final Logger log = LoggerFactory.getLogger(AdventureSocketEventProcessor.class);

    private GameRoomRepository gameRoomRepository;

    @Override
    public AdventureResponseData action(Q data, SocketClient socketClient) {
        SocketChannel socketChannel = socketClient.getSocketChannel();

        //todo может убрать проверку или что то сделать с ней
        if (!socketChannel.isConnected()) {
            throw new IllegalStateException("Client is disconnect " + socketChannel);
        }

        GameRoom gameRoom = gameRoomRepository.getGameRoom(socketClient)
                .orElseThrow(() -> new IllegalStateException("GameRoom not found for " + socketClient.getLogin()));

        if (gameRoom.getIsGameOver().get()) {
            log.info("GameRoom is GameOver for {}", socketClient.getLogin());
        }

        Lock lock = gameRoom.getLock();
        lock.lock();
        try {
            processEvent(data, gameRoom);
        } finally {
            lock.unlock();
        }
        return AdventureResponseData.of(gameRoom);
    }

    protected abstract void processEvent(Q data, GameRoom gameRoom);

    @Autowired
    public void setGameRoomRepository(GameRoomRepository gameRoomRepository) {
        this.gameRoomRepository = gameRoomRepository;
    }
}

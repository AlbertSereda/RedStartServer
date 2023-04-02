package com.redstart.server.core.message.processor.adventure;

import com.redstart.server.core.SocketClient;
import com.redstart.server.core.gamemechanics.GameRoom;
import com.redstart.server.core.message.SocketMessage;
import com.redstart.server.core.message.processor.AuthSocketEventProcessor;

public abstract class AdventureSocketEventProcessor extends AuthSocketEventProcessor {
    @Override
    protected void process(SocketMessage socketMessage, SocketClient socketClient) {

    }

    protected abstract void processEvent(SocketMessage socketMessage, GameRoom gameRoom);
}

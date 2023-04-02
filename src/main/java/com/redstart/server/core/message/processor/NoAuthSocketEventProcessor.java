package com.redstart.server.core.message.processor;

import com.redstart.server.core.gamemechanics.GameRoom;
import com.redstart.server.core.message.SocketMessage;

public abstract class NoAuthSocketEventProcessor implements ISocketEventProcessor{
    @Override
    public void process(SocketMessage socketMessage, GameRoom gameRoom) {

    }
}

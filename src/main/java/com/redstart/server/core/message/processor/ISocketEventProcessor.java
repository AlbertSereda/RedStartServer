package com.redstart.server.core.message.processor;

import com.redstart.server.core.gamemechanics.GameRoom;
import com.redstart.server.core.message.SocketMessage;

public interface ISocketEventProcessor {
    void process(SocketMessage socketMessage, GameRoom gameRoom);

    String getEventType();
}

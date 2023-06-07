package com.redstart.server.core.socket.message.processor.adventure;

import com.redstart.server.core.gamemechanics.GameRoom;
import com.redstart.server.core.gamemechanics.GameState;
import com.redstart.server.core.socket.message.SocketEventType;
import com.redstart.server.core.socket.message.requestdata.adventure.LeavePlayerRequestData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LeavePlayerEventProcessor extends AdventureSocketEventProcessor<LeavePlayerRequestData> {
    private static final Logger log = LoggerFactory.getLogger(LeavePlayerEventProcessor.class);

    @Override
    public SocketEventType getEventType() {
        return SocketEventType.ADVENTURE_LEAVE;
    }

    @Override
    protected void processEvent(LeavePlayerRequestData data, GameRoom gameRoom) {
        gameRoom.getAdventureData().setGameState(GameState.LEAVE);
        log.info("{} leaved of the game", gameRoom.getPlayer().getName());
    }
}

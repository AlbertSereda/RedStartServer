package com.redstart.server.core.message.processor.adventure;

import com.redstart.server.core.gamemechanics.GameRoom;
import com.redstart.server.core.gamemechanics.GameState;
import com.redstart.server.core.message.SocketMessage;
import com.redstart.server.core.message.processor.ISocketEventProcessor;
import org.springframework.stereotype.Component;

@Component
public class LeavePlayerEventProcessor implements ISocketEventProcessor {
    @Override
    public void process(SocketMessage socketMessage, GameRoom gameRoom) {
        GameState gameState = gameRoom.getAdventureData().getGameState();
        if (gameState != GameState.LOSE && gameState != GameState.WIN) {
            gameRoom.getPlayer().setHp(0);
        }
    }

    @Override
    public String getEventType() {
        return "leave";
    }
}

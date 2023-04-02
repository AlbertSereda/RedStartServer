package com.redstart.server.core.message.processor.adventure;

import com.redstart.server.core.gamemechanics.GameRoom;
import com.redstart.server.core.gamemechanics.GameState;
import com.redstart.server.core.message.SocketMessage;
import com.redstart.server.core.message.processor.ISocketEventProcessor;
import org.springframework.stereotype.Component;

@Component
public class PauseEventProcessor implements ISocketEventProcessor {
    @Override
    public void process(SocketMessage socketMessage, GameRoom gameRoom) {
        GameState currentGameState = gameRoom.getAdventureData().getGameState();
        if (currentGameState != GameState.LOSE && currentGameState != GameState.WIN) {
            gameRoom.setPause();
        }
    }

    @Override
    public String getEventType() {
        return "pause";
    }
}

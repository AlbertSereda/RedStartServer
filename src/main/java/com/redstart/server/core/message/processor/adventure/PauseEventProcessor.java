package com.redstart.server.core.message.processor.adventure;

import com.redstart.server.core.gamemechanics.GameRoom;
import com.redstart.server.core.gamemechanics.GameState;
import com.redstart.server.core.message.SocketEventType;
import com.redstart.server.core.message.requestdata.adventure.PauseRequestData;
import org.springframework.stereotype.Component;

@Component
public class PauseEventProcessor extends AdventureSocketEventProcessor<PauseRequestData> {

    @Override
    public SocketEventType getEventType() {
        return SocketEventType.ADVENTURE_PAUSE;
    }

    @Override
    protected void processEvent(PauseRequestData data, GameRoom gameRoom) {
        GameState currentGameState = gameRoom.getAdventureData().getGameState();
        if (currentGameState != GameState.LOSE && currentGameState != GameState.WIN) {
            gameRoom.setPause();
        }
    }
}

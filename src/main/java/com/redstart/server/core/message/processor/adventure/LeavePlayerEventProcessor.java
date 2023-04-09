package com.redstart.server.core.message.processor.adventure;

import com.redstart.server.core.gamemechanics.GameRoom;
import com.redstart.server.core.gamemechanics.GameState;
import com.redstart.server.core.message.SocketEventType;
import com.redstart.server.core.message.requestdata.adventure.LeavePlayerRequestData;
import org.springframework.stereotype.Component;

@Component
public class LeavePlayerEventProcessor extends AdventureSocketEventProcessor<LeavePlayerRequestData> {

    @Override
    public SocketEventType getEventType() {
        return SocketEventType.ADVENTURE_LEAVE;
    }

    @Override
    protected void processEvent(LeavePlayerRequestData data, GameRoom gameRoom) {
        GameState gameState = gameRoom.getAdventureData().getGameState();
        if (gameState != GameState.LOSE && gameState != GameState.WIN) {
            //TODO или меняем игровое состояние на lose, а сам game over обрабатываем в GameOverProcessor
            gameRoom.getPlayer().setHp(0);
        }
    }
}

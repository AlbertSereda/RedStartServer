package com.redstart.server.core.message.processor.adventure;

import com.redstart.server.core.gamemechanics.GameLogic;
import com.redstart.server.core.gamemechanics.GameRoom;
import com.redstart.server.core.gamemechanics.GameState;
import com.redstart.server.core.message.SocketMessage;
import com.redstart.server.core.message.processor.ISocketEventProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class StepEventProcessor implements ISocketEventProcessor {
    private static final Logger log = LoggerFactory.getLogger(StepEventProcessor.class);

    @Override
    public void process(SocketMessage socketMessage, GameRoom gameRoom) {
        if (gameRoom.getAdventureData().getGameState() == GameState.RESUME) {
            GameLogic gameLogic = gameRoom.getGameLogic();
            try {
                int nameBlockDestroyed = Integer.parseInt(socketMessage.getData());
                if (nameBlockDestroyed > 0) {
                    gameLogic.playerMove(gameRoom, nameBlockDestroyed);
                }
            } catch (NumberFormatException e) {
                log.info("Error convert String to int - {}", socketMessage.getData());
            }
        }
    }

    @Override
    public String getEventType() {
        return "adventureStep";
    }
}

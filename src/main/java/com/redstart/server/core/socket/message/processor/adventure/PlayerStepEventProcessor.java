package com.redstart.server.core.socket.message.processor.adventure;

import com.redstart.server.core.gamemechanics.GameLogic;
import com.redstart.server.core.gamemechanics.GameRoom;
import com.redstart.server.core.gamemechanics.GameState;
import com.redstart.server.core.socket.message.SocketEventType;
import com.redstart.server.core.socket.message.requestdata.adventure.StepRequestData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PlayerStepEventProcessor extends AdventureSocketEventProcessor<StepRequestData> {
    private static final Logger log = LoggerFactory.getLogger(PlayerStepEventProcessor.class);

    @Override
    public SocketEventType getEventType() {
        return SocketEventType.ADVENTURE_STEP;
    }

    @Override
    protected void processEvent(StepRequestData data, GameRoom gameRoom) {
        if (gameRoom.getAdventureData().getGameState() == GameState.RESUME) {
//            gameRoom.getPlayer().getBlastedBlocks().clear();
//            gameRoom.getPlayer().getSpawnedBlocks().clear();

            GameLogic gameLogic = gameRoom.getGameLogic();

            int nameBlockDestroyed = data.getNameBlockDestroyed();
            if (nameBlockDestroyed > 0) {
                gameLogic.playerMove(gameRoom, nameBlockDestroyed);
            }
        }
    }
}

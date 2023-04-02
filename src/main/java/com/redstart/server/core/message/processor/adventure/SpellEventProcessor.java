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
public class SpellEventProcessor implements ISocketEventProcessor {
    private static final Logger log = LoggerFactory.getLogger(SpellEventProcessor.class);

    @Override
    public void process(SocketMessage socketMessage, GameRoom gameRoom) {
        if (gameRoom.getAdventureData().getGameState() == GameState.RESUME) {
            GameLogic gameLogic = gameRoom.getGameLogic();
            String nameSpell = socketMessage.getData();
            gameLogic.spellMove(gameRoom, nameSpell);
        }
    }

    @Override
    public String getEventType() {
        return "adventureSpell";
    }
}

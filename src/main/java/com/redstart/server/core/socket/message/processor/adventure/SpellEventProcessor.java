package com.redstart.server.core.socket.message.processor.adventure;

import com.redstart.server.core.gamemechanics.GameLogic;
import com.redstart.server.core.gamemechanics.GameRoom;
import com.redstart.server.core.gamemechanics.GameState;
import com.redstart.server.core.socket.message.SocketEventType;
import com.redstart.server.core.socket.message.requestdata.adventure.SpellRequestData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SpellEventProcessor extends AdventureSocketEventProcessor<SpellRequestData> {
    private static final Logger log = LoggerFactory.getLogger(SpellEventProcessor.class);

    @Override
    public SocketEventType getEventType() {
        return SocketEventType.ADVENTURE_SPELL;
    }

    @Override
    protected void processEvent(SpellRequestData data, GameRoom gameRoom) {
        if (gameRoom.getAdventureData().getGameState() == GameState.RESUME) {
            GameLogic gameLogic = gameRoom.getGameLogic();
            String nameSpell = data.getNameSpell();
            gameLogic.spellMove(gameRoom, nameSpell);
        }
    }
}

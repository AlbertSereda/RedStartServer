package com.redstart.server.core.message.processor.adventure;

import com.redstart.server.core.gamemechanics.GameRoom;
import com.redstart.server.core.message.SocketEventType;
import com.redstart.server.core.message.requestdata.adventure.StepRequestData;
import org.springframework.stereotype.Component;

@Component
public class MonsterStepEventProcessor extends AdventureSocketEventProcessor<StepRequestData> {
    @Override
    public SocketEventType getEventType() {
        return SocketEventType.ADVENTURE_MONSTER_STEP;
    }

    @Override
    protected void processEvent(StepRequestData data, GameRoom gameRoom) {
        gameRoom.getMonster().getMonsterMoveLogic().monsterMove();
    }
}

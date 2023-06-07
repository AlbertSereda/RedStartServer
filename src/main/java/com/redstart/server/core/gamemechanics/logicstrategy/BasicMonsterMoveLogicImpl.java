package com.redstart.server.core.gamemechanics.logicstrategy;

import com.redstart.server.core.gamemechanics.GameLogic;
import com.redstart.server.core.gamemechanics.GameRoom;
import com.redstart.server.core.gamemechanics.logicstrategy.interfaces.MonsterMoveLogic;

public class BasicMonsterMoveLogicImpl implements MonsterMoveLogic {

    private final GameRoom gameRoom;

    public BasicMonsterMoveLogicImpl(GameRoom gameRoom) {
        this.gameRoom = gameRoom;
    }

    public void monsterMove() {
        GameLogic gameLogic = gameRoom.getGameLogic();
        //int damage = (int) (Math.random() * 10) + 20;
        int damage = 20;
        gameLogic.decrementPlayerHp(gameRoom.getPlayer(), damage);
    }
}
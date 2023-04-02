package com.redstart.server.core.gamemechanics.block;

import com.redstart.server.core.gamemechanics.GameRoom;

public interface ColorBlock {
    void executeAction(GameRoom gameRoom);

    Integer getNumberBlock();
}

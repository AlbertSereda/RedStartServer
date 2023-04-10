package com.redstart.server.core.gamemechanics.block;

import com.redstart.server.core.gamemechanics.GameRoom;
import com.redstart.server.core.socket.jsonclasses.Monster;
import com.redstart.server.core.socket.jsonclasses.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RedColorBlock implements ColorBlock {
    private static final Logger log = LoggerFactory.getLogger(RedColorBlock.class);

    @Override
    public void executeAction(GameRoom gameRoom) {
        Player player = gameRoom.getPlayer();
        Monster monster = gameRoom.getMonster();

        int countDamage = player.getBlastedBlocks().size();
        if (countDamage >= 10) {
            countDamage *= 2;
        }
        gameRoom.getGameLogic().decrementMonsterHp(monster, countDamage);
    }

    @Override
    public Integer getNumberBlock() {
        return 1;
    }
}

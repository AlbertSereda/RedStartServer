package com.redstart.server.core.gamemechanics.block;

import com.redstart.server.core.gamemechanics.GameRoom;
import com.redstart.server.core.socket.jsonclasses.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class YellowColorBlock implements ColorBlock {
    private static final Logger log = LoggerFactory.getLogger(YellowColorBlock.class);

    @Override
    public void executeAction(GameRoom gameRoom) {
        Player player = gameRoom.getPlayer();

        int countChoose = player.getBlastedBlocks().size();
        player.setShield(player.getShield() + countChoose);
    }

    @Override
    public Integer getNumberBlock() {
        return 0;
    }
}

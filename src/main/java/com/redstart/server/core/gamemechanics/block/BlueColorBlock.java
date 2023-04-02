package com.redstart.server.core.gamemechanics.block;

import com.redstart.server.core.SocketHandler;
import com.redstart.server.core.gamemechanics.GameRoom;
import com.redstart.server.core.jsonclasses.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class BlueColorBlock implements ColorBlock {
    private static final Logger log = LoggerFactory.getLogger(BlueColorBlock.class);

    @Override
    public void executeAction(GameRoom gameRoom) {
        Player player = gameRoom.getPlayer();

        int countChoose = player.getBlastedBlocks().size();
        player.setMana(player.getMana() + countChoose);
    }

    @Override
    public Integer getNumberBlock() {
        return 3;
    }
}

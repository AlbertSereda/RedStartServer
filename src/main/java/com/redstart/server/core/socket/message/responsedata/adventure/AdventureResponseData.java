package com.redstart.server.core.socket.message.responsedata.adventure;

import com.redstart.server.core.gamemechanics.GameRoom;
import com.redstart.server.core.gamemechanics.GameState;
import com.redstart.server.core.socket.jsonclasses.Monster;
import com.redstart.server.core.socket.jsonclasses.Player;
import com.redstart.server.core.socket.message.responsedata.ISocketMessageResponseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdventureResponseData implements ISocketMessageResponseData {
    private Player player;
    private Monster monster;
    private GameState gameState;

    public static AdventureResponseData of(GameRoom gameRoom) {
        AdventureResponseData data = new AdventureResponseData();
        Player responsePlayer = new Player();
        responsePlayer.setName(gameRoom.getPlayer().getName());
        responsePlayer.setHp(gameRoom.getPlayer().getHp());
        responsePlayer.setMana(gameRoom.getPlayer().getMana());
        responsePlayer.setShield(gameRoom.getPlayer().getShield());
        responsePlayer.setBlastedBlocks(new ArrayList<>(gameRoom.getPlayer().getBlastedBlocks()));
        responsePlayer.setSpawnedBlocks(new ArrayList<>(gameRoom.getPlayer().getSpawnedBlocks()));
        responsePlayer.setAvailableSpells(gameRoom.getPlayer().getAvailableSpells());
        responsePlayer.setEarnedMoney(gameRoom.getPlayer().getEarnedMoney());

        data.setPlayer(responsePlayer);
        data.setMonster(gameRoom.getMonster());
        data.setGameState(gameRoom.getAdventureData().getGameState());
        return data;
    }
}

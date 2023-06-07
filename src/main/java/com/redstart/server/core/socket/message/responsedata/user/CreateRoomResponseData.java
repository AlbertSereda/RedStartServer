package com.redstart.server.core.socket.message.responsedata.user;

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
public class CreateRoomResponseData implements ISocketMessageResponseData {
    private Player player;
    private Monster monster;
    private GameState gameState;

    //todo убрать этот класс, использовать AdventureResponseData
    public static CreateRoomResponseData of(GameRoom gameRoom) {
        CreateRoomResponseData data = new CreateRoomResponseData();

        Player responsePlayer = new Player();
        responsePlayer.setName(gameRoom.getPlayer().getName());
        responsePlayer.setHp(gameRoom.getPlayer().getHp());
        responsePlayer.setMana(gameRoom.getPlayer().getMana());
        responsePlayer.setShield(gameRoom.getPlayer().getShield());
        responsePlayer.setBlastedBlocks(new ArrayList<>());
        responsePlayer.setSpawnedBlocks(new ArrayList<>(gameRoom.getPlayer().getSpawnedBlocks()));
        responsePlayer.setAvailableSpells(gameRoom.getPlayer().getAvailableSpells());

        data.setPlayer(responsePlayer);
        data.setMonster(gameRoom.getMonster());
        data.setGameState(gameRoom.getAdventureData().getGameState());

        return data;
    }
}

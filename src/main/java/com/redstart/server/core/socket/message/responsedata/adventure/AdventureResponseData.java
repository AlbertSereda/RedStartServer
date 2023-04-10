package com.redstart.server.core.socket.message.responsedata.adventure;

import com.redstart.server.core.gamemechanics.GameRoom;
import com.redstart.server.core.gamemechanics.GameState;
import com.redstart.server.core.socket.jsonclasses.Monster;
import com.redstart.server.core.socket.jsonclasses.Player;
import com.redstart.server.core.socket.message.responsedata.ISocketMessageResponseData;

public class AdventureResponseData implements ISocketMessageResponseData {
    private Player player;
    private Monster monster;
    private GameState gameState;

    public AdventureResponseData(Player player, Monster monster, GameState gameState) {
        this.player = player;
        this.monster = monster;
        this.gameState = gameState;
    }

    public static AdventureResponseData of(GameRoom gameRoom) {
        return gameRoom.getAdventureData();
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Monster getMonster() {
        return monster;
    }

    public void setMonster(Monster monster) {
        this.monster = monster;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
}

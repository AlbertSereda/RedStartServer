package com.redstart.server.core.jsonclasses;

import com.redstart.server.core.gamemechanics.GameState;

public class AdventureData {
    private Player player;
    private Monster monster;

    private GameState gameState;

    public AdventureData(Player player, Monster monster, GameState gameState) {
        this.player = player;
        this.monster = monster;
        this.gameState = gameState;
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

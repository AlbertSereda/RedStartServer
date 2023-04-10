package com.redstart.server.core.gamemechanics;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.redstart.server.core.gamemechanics.logicstrategy.BasicMonsterMoveLogicImpl;
import com.redstart.server.core.gamemechanics.logicstrategy.BasicUpdateSpeedLogicImpl;
import com.redstart.server.core.gamemechanics.spells.ArrowSpell;
import com.redstart.server.core.gamemechanics.spells.FireBallSpell;
import com.redstart.server.core.gamemechanics.spells.NextDamageProtectionSpell;
import com.redstart.server.core.gamemechanics.spells.StanSpell;
import com.redstart.server.core.gamemechanics.spells.interfaces.WithTimeSpell;
import com.redstart.server.core.jsonclasses.AdventureData;
import com.redstart.server.core.jsonclasses.Monster;
import com.redstart.server.core.jsonclasses.Player;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GameRoom {
    private Player player;
    private Monster monster;

    private final Lock lock;

    private GameLogic gameLogic;

    private final AdventureData adventureData;

    private AtomicBoolean isGameOver;

    private long timeSetPause;

    private long lastAddToBuffer;

    public GameRoom(GameLogic gameLogic) {
        isGameOver = new AtomicBoolean(false);
        player = new Player("RedStart", 100, 100, 0);
        player.addAvailableSpell(new FireBallSpell(this,
                20,
                20));
        player.addAvailableSpell(new StanSpell(this,
                20,
                5000,
                0));
        player.addAvailableSpell(new NextDamageProtectionSpell(this,
                20,
                0));
        player.addAvailableSpell(new ArrowSpell(this,
                10,
                10));

        monster = new Monster(
                "Sladkoeshka",
                100,
                10000,
                new BasicMonsterMoveLogicImpl(this),
                new BasicUpdateSpeedLogicImpl());
        lock = new ReentrantLock();
        this.gameLogic = gameLogic;
        gameLogic.fillFieldForServer(player);
        adventureData = new AdventureData(player, monster, GameState.RESUME);

        lastAddToBuffer = System.currentTimeMillis();
    }

    public void pause() {
    }

    public void setPause() {
        if (adventureData.getGameState() == GameState.RESUME) {
            adventureData.setGameState(GameState.PAUSE);
            timeSetPause = System.currentTimeMillis();
        } else {
            long timePassedSetPause = System.currentTimeMillis() - timeSetPause;
            monster.setTimeCreation(monster.getTimeCreation() + timePassedSetPause);
            List<WithTimeSpell> activeSpells = player.getActiveSpellForServer();
            for (WithTimeSpell spell : activeSpells) {
                spell.setTimeCreation(spell.getTimeCreation() + timePassedSetPause);
            }
            timeSetPause = 0;
            adventureData.setGameState(GameState.RESUME);
        }
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

    public GameLogic getGameLogic() {
        return gameLogic;
    }

    public void setGameLogic(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
    }

    public AdventureData getAdventureData() {
        return adventureData;
    }

    public Lock getLock() {
        return lock;
    }

    public long getTimeSetPause() {
        return timeSetPause;
    }

    public void setTimeSetPause(long timeSetPause) {
        this.timeSetPause = timeSetPause;
    }

    public AtomicBoolean getIsGameOver() {
        return isGameOver;
    }

    public void setIsGameOver(AtomicBoolean isGameOver) {
        this.isGameOver = isGameOver;
    }

    public long getLastAddToBuffer() {
        return lastAddToBuffer;
    }

    public void setLastAddToBuffer(long lastAddToBuffer) {
        this.lastAddToBuffer = lastAddToBuffer;
    }
}

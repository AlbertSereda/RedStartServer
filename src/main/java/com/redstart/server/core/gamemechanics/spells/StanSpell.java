package com.redstart.server.core.gamemechanics.spells;

import com.redstart.server.core.gamemechanics.GameRoom;
import com.redstart.server.core.gamemechanics.logicstrategy.interfaces.UpdateSpeedLogic;
import com.redstart.server.core.gamemechanics.spells.interfaces.WithTimeSpell;
import com.redstart.server.core.socket.jsonclasses.Monster;
import com.redstart.server.core.socket.jsonclasses.SpellJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StanSpell implements WithTimeSpell, UpdateSpeedLogic {
    private static final Logger log = LoggerFactory.getLogger(StanSpell.class);
    private GameRoom gameRoom;
    private final Integer cost;

    private final long durationTime;
    private final Integer damage;

    private long timeCreation;
    private Long timeToEnd;
    private UpdateSpeedLogic oldUpdateSpeedLogic;

    private long timeMoveMonsterPassed;

    private boolean isActive;
    private final SpellJson spellJson;

    public StanSpell(Integer cost, long durationTime, Integer damage) {
        this.cost = cost;
        this.durationTime = durationTime;
        timeToEnd = 0L;
        this.damage = damage;
        isActive = false;
        spellJson = SpellJson.spellJsonConverter(this);
    }

    @Override
    public void activate() {
        decrementMana(gameRoom.getPlayer(), cost);
        resetSpell();
        timeCreation = System.currentTimeMillis();

        if (!isActive) {
            isActive = true;
            Monster monster = gameRoom.getMonster();
            oldUpdateSpeedLogic = monster.getUpdateSpeedLogic();

            timeMoveMonsterPassed = System.currentTimeMillis() - monster.getTimeCreation();
            monster.setUpdateSpeedLogic(this);

            gameRoom.getPlayer().addActiveSpell(this);

        }
    }

    @Override
    public void deactivate() {
        Monster monster = gameRoom.getMonster();
        monster.setUpdateSpeedLogic(oldUpdateSpeedLogic);
        timeToEnd = 0L;
        spellJson.setTimeToEnd(0L);
        isActive = false;
    }

    @Override
    public void setTimeCreation(long timeCreation) {
        this.timeCreation = timeCreation;
    }

    @Override
    public long getTimeCreation() {
        return timeCreation;
    }

    @Override
    public void update() {
        long timePassed = System.currentTimeMillis() - timeCreation;
        timeToEnd = durationTime - timePassed;
        spellJson.setTimeToEnd(timeToEnd);
    }

    @Override
    public int updateCurrentSpeed(long maxSpeed, long timeCreation) {
        long updateTimeCreation = System.currentTimeMillis() - timeMoveMonsterPassed;

        Monster monster = gameRoom.getMonster();
        monster.setTimeCreation(updateTimeCreation);

        return monster.getCurrentSpeed();
    }

    @Override
    public int getCost() {
        return cost;
    }


    @Override
    public long getTimeToEnd() {
        return timeToEnd;
    }


    @Override
    public int getDamage() {
        return damage;
    }

    @Override
    public String getNameSpell() {
        return "stan";
    }

    @Override
    public SpellJson getSpellJson() {
        return spellJson;
    }

    @Override
    public void setGameRoom(GameRoom gameRoom) {
        this.gameRoom = gameRoom;
    }

    @Override
    public void resetSpell() {
        timeToEnd = durationTime;
        spellJson.setTimeToEnd(durationTime);
    }

    @Override
    public long getDurationTime() {
        return durationTime;
    }
}

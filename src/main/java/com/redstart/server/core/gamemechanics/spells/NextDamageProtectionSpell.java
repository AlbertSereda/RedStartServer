package com.redstart.server.core.gamemechanics.spells;

import com.redstart.server.core.gamemechanics.GameRoom;
import com.redstart.server.core.gamemechanics.logicstrategy.interfaces.MonsterMoveLogic;
import com.redstart.server.core.gamemechanics.spells.interfaces.DelayedSpell;
import com.redstart.server.core.gamemechanics.spells.interfaces.WithTimeSpell;
import com.redstart.server.core.jsonclasses.Monster;
import com.redstart.server.core.jsonclasses.SpellJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NextDamageProtectionSpell implements DelayedSpell, MonsterMoveLogic, WithTimeSpell {
    private static final Logger log = LoggerFactory.getLogger(NextDamageProtectionSpell.class);
    private final GameRoom gameRoom;
    private final int cost;
    private final int damage;
    private MonsterMoveLogic oldMonsterMoveLogic;
    private boolean isActive = false;
    private long timeToEnd;
    private long durationTime;
    private final SpellJson spellJson;

    public NextDamageProtectionSpell(GameRoom gameRoom, int cost, int damage) {
        this.gameRoom = gameRoom;
        this.cost = cost;
        this.damage = damage;
        durationTime = 1L;
        timeToEnd = 0L;
        spellJson = SpellJson.spellJsonConverter(this);
    }

    public void activate() {
        if (!isActive) {
            resetSpell();
            isActive = true;
            decrementMana(gameRoom.getPlayer(), cost);

            Monster monster = gameRoom.getMonster();
            oldMonsterMoveLogic = monster.getMonsterMoveLogic();
            monster.setMonsterMoveLogic(this);

            gameRoom.getPlayer().addActiveSpell(this);
        }
    }

    public int getCost() {
        return cost;
    }

    public int getDamage() {
        return damage;
    }

    @Override
    public String getNameSpell() {
        return "nextDamageProtection";
    }

    @Override
    public SpellJson getSpellJson() {
        return spellJson;
    }

    public void monsterMove() {
        timeToEnd = 0L;
        Monster monster = gameRoom.getMonster();
        monster.setMonsterMoveLogic(oldMonsterMoveLogic);
    }

    @Override
    public long getTimeToEnd() {
        return timeToEnd;
    }

    @Override
    public void resetSpell() {
        timeToEnd = durationTime;
        spellJson.setTimeToEnd(timeToEnd);
    }

    @Override
    public void update() {
    }

    @Override
    public void deactivate() {
        isActive = false;
        timeToEnd = 0L;
        spellJson.setTimeToEnd(timeToEnd);
    }

    @Override
    public void setTimeCreation(long timeCreation) {
    }

    @Override
    public long getTimeCreation() {
        return 0;
    }

    @Override
    public long getDurationTime() {
        return durationTime;
    }
}

package com.redstart.server.core.gamemechanics.spells;

import com.redstart.server.core.gamemechanics.GameRoom;
import com.redstart.server.core.gamemechanics.spells.interfaces.OneTimeSpell;
import com.redstart.server.core.socket.jsonclasses.SpellJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArrowSpell implements OneTimeSpell {
    private static final Logger log = LoggerFactory.getLogger(ArrowSpell.class);
    private GameRoom gameRoom;
    private final int cost;
    private final int damage;
    private final SpellJson spellJson;

    public ArrowSpell(int cost, int damage) {
        this.cost = cost;
        this.damage = damage;
        spellJson = SpellJson.spellJsonConverter(this);
    }

    public void activate() {
        decrementMana(gameRoom.getPlayer(), cost);
        gameRoom.getGameLogic().decrementMonsterHp(gameRoom.getMonster(), damage);
    }

    public int getCost() {
        return cost;
    }

    public int getDamage() {
        return damage;
    }

    @Override
    public String getNameSpell() {
        return "arrow";
    }

    @Override
    public SpellJson getSpellJson() {
        return spellJson;
    }

    @Override
    public void setGameRoom(GameRoom gameRoom) {
        this.gameRoom = gameRoom;
    }
}

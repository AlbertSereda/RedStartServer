package com.redstart.server.core.socket.jsonclasses;

import com.redstart.server.core.gamemechanics.spells.interfaces.Spell;
import com.redstart.server.core.gamemechanics.spells.interfaces.WithTimeSpell;

public class SpellJson {
    private String name;
    private int cost;
    private int damage;
    private long durationTime;
    private long timeToEnd;

    public SpellJson() {
    }

    public SpellJson(String name, int cost, int damage, long durationTime, long timeToEnd) {
        this.name = name;
        this.cost = cost;
        this.damage = damage;
        this.durationTime = durationTime;
        this.timeToEnd = timeToEnd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public long getDurationTime() {
        return durationTime;
    }

    public void setDurationTime(long durationTime) {
        this.durationTime = durationTime;
    }

    public long getTimeToEnd() {
        return timeToEnd;
    }

    public void setTimeToEnd(long timeToEnd) {
        this.timeToEnd = timeToEnd;
    }

    public static SpellJson spellJsonConverter(Spell spell) {
        SpellJson spellJson = new SpellJson();
        spellJson.setName(spell.getNameSpell());
        spellJson.setCost(spell.getCost());
        spellJson.setDamage(spell.getDamage());

        if (spell instanceof WithTimeSpell withTimeSpell) {
            spellJson.setDurationTime(withTimeSpell.getDurationTime());
            spellJson.setTimeToEnd(withTimeSpell.getTimeToEnd());
        } else {
            spellJson.setDurationTime(0);
            spellJson.setTimeToEnd(0L);
        }
        return spellJson;
    }
}

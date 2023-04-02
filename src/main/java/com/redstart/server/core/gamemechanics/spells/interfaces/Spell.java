package com.redstart.server.core.gamemechanics.spells.interfaces;

import com.redstart.server.core.jsonclasses.Player;
import com.redstart.server.core.jsonclasses.SpellJson;

public interface Spell {
    void activate();

    int getCost();

    int getDamage();

    default void decrementMana(Player player, int cost) {
        player.setMana(player.getMana() - cost);
    }

    String getNameSpell();

    SpellJson getSpellJson();
}

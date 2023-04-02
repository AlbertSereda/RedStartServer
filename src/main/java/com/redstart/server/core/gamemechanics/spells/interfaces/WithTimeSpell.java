package com.redstart.server.core.gamemechanics.spells.interfaces;

public interface WithTimeSpell extends Spell {

    long getTimeToEnd();

    void resetSpell();

    void update();

    void deactivate();

    void setTimeCreation(long timeCreation);

    long getTimeCreation();

    long getDurationTime();
}

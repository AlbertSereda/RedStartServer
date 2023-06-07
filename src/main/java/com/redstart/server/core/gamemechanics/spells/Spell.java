package com.redstart.server.core.gamemechanics.spells;

public enum Spell {
    ARROW("arrow");

    private final String name;

    Spell(String name) {
        this.name = name;
    }

    public Spell getByName(String name) {
        for (Spell spell : Spell.values()) {
            if (spell.name.equals(name)) {
                return spell;
            }
        }
        throw new IllegalArgumentException("get TaskType by id error: " + name);
    }
}

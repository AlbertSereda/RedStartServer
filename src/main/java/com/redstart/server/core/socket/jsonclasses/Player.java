package com.redstart.server.core.socket.jsonclasses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.redstart.server.core.gamemechanics.spells.interfaces.ISpell;
import com.redstart.server.core.gamemechanics.spells.interfaces.WithTimeSpell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Player {
    @JsonIgnore
    private static final Logger log = LoggerFactory.getLogger(Player.class);
    private String name;
    private int hp;
    private int mana;
    private int shield;
    private int earnedMoney = 0;
    @JsonIgnore
    private int[][][] fieldForServer; //имя, индекс, цвет
    private List<Integer> blastedBlocks; //имена уничтоженных
    private List<int[]> spawnedBlocks; //массив заспавненых имя, индекс, цвет.
    @JsonIgnore
    private Map<String, ISpell> availableSpellsForServer;
    @JsonIgnore
    private List<WithTimeSpell> activeSpellForServer;

    private Set<SpellJson> availableSpells;

    //private final Set<SpellJson> activeSpells;

    @JsonIgnore
    private int nextNameBlock = 1;

    public Player() {
    }

    public Player(String name, int hp, int mana, int shield) {
        this.name = name;
        this.hp = hp;
        this.mana = mana;
        this.shield = shield;
        blastedBlocks = new ArrayList<>();
        spawnedBlocks = new ArrayList<>();
        availableSpellsForServer = new HashMap<>();
        activeSpellForServer = new CopyOnWriteArrayList<>();

        availableSpells = new HashSet<>();
        //activeSpells = new HashSet<>();
    }

    public void addAvailableSpell(ISpell spell) {
        availableSpellsForServer.put(spell.getNameSpell(), spell);
        //availableSpells.add(spell.getNameSpell());

        availableSpells.add(spell.getSpellJson());
    }

    public void addActiveSpell(WithTimeSpell withTimeSpell) {
        activeSpellForServer.add(withTimeSpell);
        //activeSpells.add(withTimeSpell.getNameSpell());

        //activeSpells.add(withTimeSpell.getSpellJson());
//        log.info("Добавили заклинание в активные: " + withTimeSpell.getNameSpell());
    }

    public void removeActiveSpell(WithTimeSpell withTimeSpell) {
        activeSpellForServer.remove(withTimeSpell);
        //activeSpells.remove(withTimeSpell.getNameSpell());

        //activeSpells.remove(withTimeSpell.getSpellJson());
//        log.info("Удалили заклинание из активных: " + withTimeSpell.getNameSpell());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int[][][] getFieldForServer() {
        return fieldForServer;
    }

    public void setFieldForServer(int[][][] fieldForServer) {
        this.fieldForServer = fieldForServer;
    }

    public List<Integer> getBlastedBlocks() {
        return blastedBlocks;
    }

    public void setBlastedBlocks(List<Integer> blastedBlocks) {
        this.blastedBlocks = blastedBlocks;
    }

    public List<int[]> getSpawnedBlocks() {
        return spawnedBlocks;
    }

    public void setSpawnedBlocks(List<int[]> spawnedBlocks) {
        this.spawnedBlocks = spawnedBlocks;
    }

    public int getNextNameBlock() {
        return nextNameBlock++;
    }

    public void setNextNameBlock(int nextNameBlock) {
        this.nextNameBlock = nextNameBlock;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public int getShield() {
        return shield;
    }

    public void setShield(int shield) {
        this.shield = shield;
    }

    public Map<String, ISpell> getAvailableSpellsForServer() {
        return availableSpellsForServer;
    }

    public List<WithTimeSpell> getActiveSpellForServer() {
        return activeSpellForServer;
    }

    public Set<SpellJson> getAvailableSpells() {
        return availableSpells;
    }

//    public Set<SpellJson> getActiveSpells() {
//        return activeSpells;
//    }

    public void setAvailableSpells(Set<SpellJson> availableSpells) {
        this.availableSpells = availableSpells;
    }

    public int getEarnedMoney() {
        return earnedMoney;
    }

    public void setEarnedMoney(int earnedMoney) {
        this.earnedMoney = earnedMoney;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", hp=" + hp +
                ", mana=" + mana +
                ", shield=" + shield +
                '}';
    }
}

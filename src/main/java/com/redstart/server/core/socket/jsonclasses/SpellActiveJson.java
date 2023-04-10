package com.redstart.server.core.socket.jsonclasses;

public class SpellActiveJson {
    private String name;
    private int cost;
    private int maxSpeed;
    private int currentSpeed;

    public SpellActiveJson() {
    }

    public SpellActiveJson(String name, int cost, int maxSpeed, int currentSpeed) {
        this.name = name;
        this.cost = cost;
        this.maxSpeed = maxSpeed;
        this.currentSpeed = currentSpeed;
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

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public int getCurrentSpeed() {
        return currentSpeed;
    }

    public void setCurrentSpeed(int currentSpeed) {
        this.currentSpeed = currentSpeed;
    }
}

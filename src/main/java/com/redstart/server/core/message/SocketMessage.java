package com.redstart.server.core.message;


public class SocketMessage {
    private String gameEvent;
    private String data;

    public SocketMessage() {
    }

    public SocketMessage(String gameEvent, String data) {
        this.gameEvent = gameEvent;
        this.data = data;
    }

    public String getGameEvent() {
        return gameEvent;
    }

    public void setGameEvent(String gameEvent) {
        this.gameEvent = gameEvent;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

package com.redstart.server.core.message;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.redstart.server.config.SocketMessageDeserializer;

@JsonPropertyOrder({"gameEvent", "data"})
public class SocketMessage {
    @JsonProperty("gameEvent")
    private SocketEventType eventType;
    @JsonDeserialize(using = SocketMessageDeserializer.class)
    private ISocketMessageData data;

    public SocketMessage() {
    }

    public SocketMessage(SocketEventType eventType, ISocketMessageData data) {
        this.eventType = eventType;
        this.data = data;
    }

    public SocketEventType getEventType() {
        return eventType;
    }

    public void setEventType(SocketEventType eventType) {
        this.eventType = eventType;
    }

    public ISocketMessageData getData() {
        return data;
    }

    public void setData(ISocketMessageData data) {
        this.data = data;
    }
}

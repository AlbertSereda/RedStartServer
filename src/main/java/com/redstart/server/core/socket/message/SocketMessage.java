package com.redstart.server.core.socket.message;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.redstart.server.config.SocketMessageDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonPropertyOrder({"gameEvent", "data"})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocketMessage {
    @JsonProperty("gameEvent")
    private SocketEventType eventType;
    @JsonDeserialize(using = SocketMessageDeserializer.class)
    private ISocketMessageData data;
    private String error;
}

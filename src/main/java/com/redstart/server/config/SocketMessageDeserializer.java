package com.redstart.server.config;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.redstart.server.core.message.ISocketMessageData;
import com.redstart.server.core.message.SocketEventType;
import com.redstart.server.core.message.SocketMessage;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SocketMessageDeserializer extends JsonDeserializer<ISocketMessageData> {
    @Override
    public ISocketMessageData deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        TreeNode treeNode = p.getCodec().readTree(p);
        SocketEventType eventType = ((SocketMessage) p.getParsingContext().getCurrentValue()).getEventType();

        if (eventType == null) {
            throw new IllegalStateException("Event type is null");
        }

        return p.getCodec().treeToValue(treeNode, eventType.getRequestDataClass());
    }
}

package com.redstart.server.core.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MessageHandler {
    private static final Logger log = LoggerFactory.getLogger(MessageHandler.class);

    private final ObjectMapper objectMapper;

    public MessageHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public byte[] objectToJson(Object object) {
        try {
            return objectMapper.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error convert object to JSON");
        }
    }

//    public int jsonToMessage(String message) {
//        String str = message.replaceAll("[^\\d-]", "");
//        int number = 0;
//        try {
//            number = Integer.parseInt(str);
//        } catch (NumberFormatException e) {
//            log.log(Level.INFO, "Error convert String to Int. " + e.getMessage());
//        }
//        return number;
//    }

    public SocketMessage jsonToSocketMessage(String message) {
        try {
            return objectMapper.readValue(message, SocketMessage.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error convert JSON to SocketMessage - " + message);
        }
    }

}

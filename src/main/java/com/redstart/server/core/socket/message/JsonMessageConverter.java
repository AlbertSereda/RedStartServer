package com.redstart.server.core.socket.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class JsonMessageConverter {
    private static final Logger log = LoggerFactory.getLogger(JsonMessageConverter.class);

    private final ObjectMapper objectMapper;

    public JsonMessageConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public byte[] objectToJson(Object object) {
        try {
            return objectMapper.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
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

    public SocketMessage jsonToSocketMessage(String message) throws JsonProcessingException {
        return objectMapper.readValue(message, SocketMessage.class);
    }
}

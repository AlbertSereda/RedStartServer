package com.redstart.server.core.message;

import com.redstart.server.core.SocketClient;
import com.redstart.server.core.message.processor.ISocketEventProcessor;
import com.redstart.server.core.message.responsedata.ISocketMessageResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

@Component
public class SocketMessageHandler {
    private static final Logger log = LoggerFactory.getLogger(SocketMessageHandler.class);
    private final ExecutorService executorService;
    private final JsonMessageConverter jsonMessageConverter;
    private final SocketEventFactory socketEventFactory;

    public SocketMessageHandler(@Qualifier("socketMessageHandlerExecutor") ExecutorService executorService,
                                JsonMessageConverter jsonMessageConverter,
                                SocketEventFactory socketEventFactory) {
        this.executorService = executorService;
        this.jsonMessageConverter = jsonMessageConverter;
        this.socketEventFactory = socketEventFactory;
    }

    public void handle(String message, SocketClient socketClient) {
        executorService.execute(() -> {
            //несколько сообщений могут прийти вместе, поэтому делим и отдельно с каждым работаем
            //если подряд несколько одинаковых сообщений то пропускаем их
            String[] messageSplit = message.split("\n");
            String lastString = "";

            for (String str : messageSplit) {
                if (!lastString.equals(str)) {
                    //executeMove(socketClient, Move.PLAYER, messageSplit[i]);
                    handleMessage(str, socketClient);
                    lastString = str;
                }
            }
        });
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void handleMessage(String message, SocketClient socketClient) {
        try {
            SocketMessage socketMessage = jsonMessageConverter.jsonToSocketMessage(message);
            SocketEventType eventType = socketMessage.getEventType();

            ISocketEventProcessor eventProcessor = socketEventFactory.getEventProcessor(eventType)
                    .orElseThrow(() -> new IllegalStateException("Not found ISocketEventProcessor for - " + socketMessage.getEventType()));

            ISocketMessageResponseData responseData = eventProcessor.process(socketMessage.getData(), socketClient);

            socketMessage.setData(responseData);

            socketClient.addToWriteObject(socketMessage);
        } catch (Exception e) {
            log.error("Какая то ошибка, надо обработать", e);
        }
    }
}

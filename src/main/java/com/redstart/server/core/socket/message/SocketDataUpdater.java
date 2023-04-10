package com.redstart.server.core.socket.message;

import com.redstart.server.core.socket.SocketClient;
import com.redstart.server.core.socket.SocketHandler;
import com.redstart.server.core.socket.message.processor.ISocketEventProcessor;
import com.redstart.server.core.socket.message.requestdata.ISocketMessageRequestData;
import com.redstart.server.core.socket.message.responsedata.ISocketMessageResponseData;
import com.redstart.server.core.socket.message.responsedata.adventure.AdventureResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

@Component
public class SocketDataUpdater {
    private static final Logger log = LoggerFactory.getLogger(SocketDataUpdater.class);
    private final SocketEventFactory socketEventFactory;
    private final SocketHandler socketHandler;
    private final ExecutorService executorService;
    private final JsonMessageConverter jsonMessageConverter;

    public SocketDataUpdater(SocketEventFactory socketEventFactory,
                             SocketHandler socketHandler,
                             @Qualifier("socketMessageHandlerExecutor") ExecutorService executorService,
                             JsonMessageConverter jsonMessageConverter) {
        this.socketEventFactory = socketEventFactory;
        this.socketHandler = socketHandler;
        this.executorService = executorService;
        this.jsonMessageConverter = jsonMessageConverter;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void updateData(SocketEventType eventType, ISocketMessageRequestData requestData, SocketClient socketClient) {
        executorService.execute(() -> {
            try {
                ISocketEventProcessor eventProcessor = socketEventFactory.getEventProcessor(eventType)
                        .orElseThrow(() -> new IllegalStateException("Not found ISocketEventProcessor for - " + eventType));

                ISocketMessageResponseData responseData = eventProcessor.process(requestData, socketClient);

                SocketMessage socketMessage = new SocketMessage(eventType, responseData);

                //socketMessage.setData(responseData);

                socketClient.addToWriteObject(socketMessage);
            } catch (Exception e) {
                log.error("Ошибка в SocketDataUpdater", e);
            }
        });
    }

    public void updateFrame(SocketEventType eventType, SocketClient socketClient, AdventureResponseData responseData) {
        SocketMessage socketMessage = new SocketMessage(eventType, responseData);
        socketClient.addToWriteObject(socketMessage);
    }
}

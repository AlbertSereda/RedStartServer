package com.redstart.server.core.socket.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.redstart.server.core.socket.SocketClient;
import com.redstart.server.core.socket.message.processor.ISocketEventProcessor;
import com.redstart.server.core.socket.message.responsedata.ISocketMessageResponseData;
import com.redstart.server.exception.ClientIsDisconnectException;
import com.redstart.server.exception.GameRoomNotFoundException;
import com.redstart.server.exception.NotFoundProcessorException;
import com.redstart.server.exception.UnauthorizedUserProcessException;
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
    private final ExecutorService adventureGameExecutor;
    private final ExecutorService userMessageExecutor;


    public SocketMessageHandler(@Qualifier("socketMessageHandlerExecutor") ExecutorService executorService,
                                JsonMessageConverter jsonMessageConverter,
                                SocketEventFactory socketEventFactory,
                                @Qualifier("adventureGameExecutor") ExecutorService adventureGameExecutor,
                                @Qualifier("userMessageExecutor") ExecutorService userMessageExecutor) {
        this.executorService = executorService;
        this.jsonMessageConverter = jsonMessageConverter;
        this.socketEventFactory = socketEventFactory;
        this.adventureGameExecutor = adventureGameExecutor;
        this.userMessageExecutor = userMessageExecutor;
    }

    public void handle(String message, SocketClient socketClient) {
        executorService.execute(() -> {
            //несколько сообщений могут прийти вместе, поэтому делим и отдельно с каждым работаем
            //если подряд несколько одинаковых сообщений то пропускаем их
            String[] messageSplit = message.split("\n");
            String lastString = "";

            for (String str : messageSplit) {
                if (str.equals(".")) {
                    continue;
                }
                if (!lastString.equals(str)) {
                    //executeMove(socketClient, Move.PLAYER, messageSplit[i]);
                    try {
                        SocketMessage socketMessage = jsonMessageConverter.jsonToSocketMessage(str);
                        if (socketMessage.getEventType().isAdventureType()) {
                            handleAdventureMessage(socketMessage, socketClient);
                        } else {
                            handleUserMessage(socketMessage, socketClient);
                        }
                        lastString = str;
                    } catch (JsonProcessingException e) {
                        log.error("Error convert JSON to SocketMessage : {}", str, e);
                    }
                }
            }
        });
    }

    private void handleAdventureMessage(SocketMessage socketMessage, SocketClient socketClient) {
        adventureGameExecutor.execute(() -> handleMessage(socketMessage, socketClient));
    }

    private void handleUserMessage(SocketMessage socketMessage, SocketClient socketClient) {
        userMessageExecutor.execute(() -> handleMessage(socketMessage, socketClient));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void handleMessage(SocketMessage socketMessage, SocketClient socketClient) {
        try {
            SocketEventType eventType = socketMessage.getEventType();

            ISocketEventProcessor eventProcessor = socketEventFactory.getEventProcessor(eventType)
                    .orElseThrow(() -> new NotFoundProcessorException("Not found ISocketEventProcessor for - " + socketMessage.getEventType()));

            try {
                ISocketMessageResponseData responseData = eventProcessor.process(socketMessage.getData(), socketClient);
                socketMessage.setData(responseData);
            } catch (IllegalStateException e) {
                log.info(e.getMessage(), e);
                socketMessage.setData(null);
                socketMessage.setError(e.getMessage());
            }

            if (!socketClient.getSocketChannel().isConnected()) {
                throw new ClientIsDisconnectException("Client is disconnect " + socketClient.getSocketChannel());
            }

            if (!SocketEventType.noResponse(eventType)) {
                log.info("Send to {}, message {}", socketClient.getLogin(), socketMessage);
                socketClient.addToWriteObject(socketMessage);
            }
        } catch (UnauthorizedUserProcessException | GameRoomNotFoundException e) {
            log.info(e.getMessage());
        } catch (ClientIsDisconnectException e) {
            log.info("Impossible send data", e);
        } catch (NotFoundProcessorException e) {
            log.error(e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unknown Error", e);
        }
    }
}

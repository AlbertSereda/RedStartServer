package com.redstart.server.core.socket.message;

import com.redstart.server.core.socket.SocketClient;
import com.redstart.server.core.socket.SocketHandler;
import com.redstart.server.core.socket.message.processor.ISocketEventProcessor;
import com.redstart.server.core.socket.message.requestdata.ISocketMessageRequestData;
import com.redstart.server.core.socket.message.responsedata.ErrorResponse;
import com.redstart.server.core.socket.message.responsedata.ISocketMessageResponseData;
import com.redstart.server.core.socket.message.responsedata.adventure.AdventureResponseData;
import com.redstart.server.exception.ClientIsDisconnectException;
import com.redstart.server.exception.GameRoomNotFoundException;
import com.redstart.server.exception.NotFoundProcessorException;
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
    private final ExecutorService userMessageExecutor;
    private final JsonMessageConverter jsonMessageConverter;
    private final ExecutorService adventureGameExecutor;

    public SocketDataUpdater(SocketEventFactory socketEventFactory,
                             SocketHandler socketHandler,
                             @Qualifier("userMessageExecutor") ExecutorService userMessageExecutor,
                             @Qualifier("adventureGameExecutor") ExecutorService adventureGameExecutor,
                             JsonMessageConverter jsonMessageConverter) {
        this.socketEventFactory = socketEventFactory;
        this.socketHandler = socketHandler;
        this.userMessageExecutor = userMessageExecutor;
        this.jsonMessageConverter = jsonMessageConverter;
        this.adventureGameExecutor = adventureGameExecutor;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void updateData(SocketEventType eventType,
                           ISocketMessageRequestData requestData,
                           SocketClient socketClient,
                           boolean isNeedSendToClient) {
        try {
            ISocketEventProcessor eventProcessor = socketEventFactory.getEventProcessor(eventType)
                    .orElseThrow(() -> new NotFoundProcessorException("Not found ISocketEventProcessor for - " + eventType));

            SocketMessage socketMessage = new SocketMessage();
            socketMessage.setEventType(eventType);
            try {
                ISocketMessageResponseData responseData = eventProcessor.process(requestData, socketClient);
                socketMessage.setData(responseData);
            } catch (IllegalStateException e) {
                log.info(e.getMessage(), e);
                socketMessage.setError(e.getMessage());
            }
            if (isNeedSendToClient) {
                if (!socketClient.getSocketChannel().isConnected()) {
                    throw new ClientIsDisconnectException("Client is disconnect " + socketClient.getSocketChannel());
                }

                if (eventType != SocketEventType.ADVENTURE_UPDATE_FRAME) {
                    log.info("Send to {}, message {}", socketClient.getLogin(), socketMessage);
                }
                socketClient.addToWriteObject(socketMessage);
            }


        } catch (ClientIsDisconnectException e) {
            log.info("Impossible send data", e);
        } catch (GameRoomNotFoundException e) {
            log.info(e.getMessage());
        } catch (NotFoundProcessorException e) {
            log.error(e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unknown error", e);
        }
    }

    public void updateAdventureData(SocketEventType eventType, ISocketMessageRequestData requestData, SocketClient socketClient) {
        adventureGameExecutor.execute(() -> {
            updateData(eventType, requestData, socketClient, true);
        });
    }

    public void updateUserData(SocketEventType eventType,
                               ISocketMessageRequestData requestData,
                               SocketClient socketClient) {
        updateUserData(eventType, requestData, socketClient, true);
    }

    public void updateUserData(SocketEventType eventType,
                               ISocketMessageRequestData requestData,
                               SocketClient socketClient,
                               boolean isNeedSendToClient) {
        userMessageExecutor.execute(() -> {
            updateData(eventType, requestData, socketClient, isNeedSendToClient);
        });
    }

    public void updateFrame(SocketEventType eventType,
                            SocketClient socketClient,
                            AdventureResponseData responseData) {
        adventureGameExecutor.execute(() -> {
            SocketMessage socketMessage = new SocketMessage(eventType, responseData, ErrorResponse.NO_ERROR.getErrorMessage());
            socketClient.addToWriteObject(socketMessage);
        });
    }
}

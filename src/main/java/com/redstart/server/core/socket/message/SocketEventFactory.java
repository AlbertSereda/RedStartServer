package com.redstart.server.core.socket.message;

import com.redstart.server.core.socket.message.processor.ISocketEventProcessor;
import com.redstart.server.core.socket.message.requestdata.ISocketMessageRequestData;
import com.redstart.server.core.socket.message.responsedata.ISocketMessageResponseData;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SocketEventFactory {
    private final Map<SocketEventType, ISocketEventProcessor<? extends ISocketMessageRequestData, ? extends ISocketMessageResponseData>> eventProcessorMap;

    public SocketEventFactory(Set<ISocketEventProcessor<? extends ISocketMessageRequestData, ? extends ISocketMessageResponseData>> eventProcessor) {
        this.eventProcessorMap = eventProcessor.stream()
                .collect(Collectors
                        .toMap(ISocketEventProcessor::getEventType,
                                socketEventProcessor -> socketEventProcessor));
    }

    public Optional<ISocketEventProcessor<? extends ISocketMessageData, ? extends ISocketMessageResponseData>> getEventProcessor(SocketEventType eventType) {
        return Optional.ofNullable(eventProcessorMap.get(eventType));
    }
}

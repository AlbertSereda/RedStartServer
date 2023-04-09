package com.redstart.server.core.message.processor;

import com.redstart.server.core.SocketClient;
import com.redstart.server.core.message.ISocketMessageData;
import com.redstart.server.core.message.SocketEventType;
import com.redstart.server.core.message.responsedata.ISocketMessageResponseData;

public interface ISocketEventProcessor<Q extends ISocketMessageData, R extends ISocketMessageResponseData> {
    R process(Q data, SocketClient socketClient);

    SocketEventType getEventType();
}

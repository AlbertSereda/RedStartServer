package com.redstart.server.core.socket.message.processor;

import com.redstart.server.core.socket.SocketClient;
import com.redstart.server.core.socket.message.ISocketMessageData;
import com.redstart.server.core.socket.message.SocketEventType;
import com.redstart.server.core.socket.message.responsedata.ISocketMessageResponseData;

public interface ISocketEventProcessor<Q extends ISocketMessageData, R extends ISocketMessageResponseData> {
    R process(Q data, SocketClient socketClient);

    SocketEventType getEventType();
}

package com.redstart.server.core.message.processor;

import com.redstart.server.core.SocketClient;
import com.redstart.server.core.message.ISocketMessageData;
import com.redstart.server.core.message.responsedata.ISocketMessageResponseData;

public abstract class NoAuthSocketEventProcessor<Q extends ISocketMessageData, R extends ISocketMessageResponseData>
        implements ISocketEventProcessor<Q, R> {

    @Override
    public R process(Q data, SocketClient socketClient) {
        return action(data, socketClient);
    }

    protected abstract R action(Q data, SocketClient socketClient);
}

package com.redstart.server.core.socket.message.processor;

import com.redstart.server.core.socket.SocketClient;
import com.redstart.server.core.socket.message.requestdata.ISocketMessageRequestData;
import com.redstart.server.core.socket.message.responsedata.ISocketMessageResponseData;

public abstract class AuthSocketEventProcessor<Q extends ISocketMessageRequestData, R extends ISocketMessageResponseData>
        implements ISocketEventProcessor<Q, R> {

    @Override
    public R process(Q data, SocketClient socketClient) {
        //проверяем авторизацию
        //если у клиента написан логин в socketClient, значит он авторизован
        if (socketClient.getLogin() == null) {
            throw new IllegalStateException("Unauthorized user");
        }

        return action(data, socketClient);
    }

    protected abstract R action(Q data, SocketClient socketClient);
}
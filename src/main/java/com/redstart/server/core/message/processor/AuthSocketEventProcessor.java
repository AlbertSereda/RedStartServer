package com.redstart.server.core.message.processor;

import com.redstart.server.core.SocketClient;
import com.redstart.server.core.gamemechanics.GameRoom;
import com.redstart.server.core.message.SocketMessage;

public abstract class AuthSocketEventProcessor implements ISocketEventProcessor {
    @Override
    public void process(SocketMessage socketMessage, GameRoom gameRoom) {
        //проверяем авторизацию
        //если у клиента написан логин в socketClient, значит он авторизован

    }

    protected abstract void process(SocketMessage socketMessage, SocketClient socketClient);
}

package com.redstart.server.core.socket.message.requestdata.adventure;

import com.redstart.server.core.socket.SocketClient;
import com.redstart.server.core.socket.message.requestdata.ISocketMessageRequestData;

public class GameOverRequestData implements ISocketMessageRequestData {
    private SocketClient socketClient;

    public GameOverRequestData() {
    }

    public GameOverRequestData(SocketClient socketClient) {
        this.socketClient = socketClient;
    }

    public SocketClient getSocketClient() {
        return socketClient;
    }

    public void setSocketClient(SocketClient socketClient) {
        this.socketClient = socketClient;
    }
}

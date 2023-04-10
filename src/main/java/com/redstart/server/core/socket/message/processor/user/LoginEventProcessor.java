package com.redstart.server.core.socket.message.processor.user;

import com.redstart.server.core.socket.SocketClient;
import com.redstart.server.core.socket.message.SocketEventType;
import com.redstart.server.core.socket.message.processor.NoAuthSocketEventProcessor;
import com.redstart.server.core.socket.message.requestdata.user.LoginRequestData;
import com.redstart.server.core.socket.message.responsedata.user.LoginResponseData;
import org.springframework.stereotype.Component;

@Component
public class LoginEventProcessor extends NoAuthSocketEventProcessor<LoginRequestData, LoginResponseData> {
    @Override
    public SocketEventType getEventType() {
        return SocketEventType.USER_LOGIN;
    }

    @Override
    protected LoginResponseData action(LoginRequestData data, SocketClient socketClient) {
        socketClient.setLogin(data.getLogin());
        return new LoginResponseData(data.getLogin(), 5000, 1000, "Всем привет, я разработчик этой игры. Я хочу кушац");
    }
}

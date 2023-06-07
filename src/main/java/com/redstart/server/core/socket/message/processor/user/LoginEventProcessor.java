package com.redstart.server.core.socket.message.processor.user;

import com.redstart.server.core.dto.IslandDTO;
import com.redstart.server.core.dto.UserDTO;
import com.redstart.server.core.socket.SocketClient;
import com.redstart.server.core.socket.message.SocketEventType;
import com.redstart.server.core.socket.message.processor.NoAuthSocketEventProcessor;
import com.redstart.server.core.socket.message.requestdata.user.LoginRequestData;
import com.redstart.server.core.socket.message.responsedata.user.LoginResponseData;
import com.redstart.server.database.service.IslandService;
import com.redstart.server.database.service.UserService;
import com.redstart.server.database.service.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LoginEventProcessor extends NoAuthSocketEventProcessor<LoginRequestData, LoginResponseData> {
    private static final Logger log = LoggerFactory.getLogger(LoginEventProcessor.class);
    private final UserService userService;
    private final IslandService islandService;
    private final String serverMessage;

    public LoginEventProcessor(UserService userService,
                               IslandService islandService,
                               @Value("${serverMessage}") String serverMessage) {
        this.userService = userService;
        this.islandService = islandService;
        this.serverMessage = serverMessage;
    }

    @Override
    public SocketEventType getEventType() {
        return SocketEventType.USER_LOGIN;
    }

    @Override
    protected LoginResponseData action(LoginRequestData data, SocketClient socketClient) {
        UserDTO userDTO = userService.getUser(data.getLogin(), data.getPassword());
        socketClient.setLogin(userDTO.getLogin());

        List<IslandDTO> islandsDTO = islandService.getAllIslands();
        return LoginResponseData.ofSuccess(userDTO, islandsDTO, serverMessage);
    }
}

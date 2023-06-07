package com.redstart.server.core.socket.message.processor.user;

import com.redstart.server.core.dto.IslandDTO;
import com.redstart.server.core.dto.UserDTO;
import com.redstart.server.core.socket.SocketClient;
import com.redstart.server.core.socket.message.SocketEventType;
import com.redstart.server.core.socket.message.processor.AuthSocketEventProcessor;
import com.redstart.server.core.socket.message.requestdata.user.UpdateUserRequestData;
import com.redstart.server.core.socket.message.responsedata.user.LoginResponseData;
import com.redstart.server.database.service.IslandService;
import com.redstart.server.database.service.UserDataService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UpdateUserDataEventProcessor extends AuthSocketEventProcessor<UpdateUserRequestData, LoginResponseData> {
    private final UserDataService userDataService;
    private final IslandService islandService;
    private final String serverMessage;

    public UpdateUserDataEventProcessor(UserDataService userDataService,
                                        IslandService islandService,
                                        @Value("${serverMessage}") String serverMessage) {
        this.userDataService = userDataService;
        this.islandService = islandService;
        this.serverMessage = serverMessage;
    }

    @Override
    protected LoginResponseData action(UpdateUserRequestData data, SocketClient socketClient) {
        UserDTO userDTO = userDataService.getUserdata(socketClient.getLogin());

        List<IslandDTO> islandsDTO = islandService.getAllIslands();
        return LoginResponseData.ofSuccess(userDTO, islandsDTO, serverMessage);
    }

    @Override
    public SocketEventType getEventType() {
        return SocketEventType.USER_UPDATE_DATA;
    }
}

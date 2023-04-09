package com.redstart.server.core.message.processor.user;

import com.redstart.server.core.SocketClient;
import com.redstart.server.core.message.SocketEventType;
import com.redstart.server.core.message.processor.NoAuthSocketEventProcessor;
import com.redstart.server.core.message.requestdata.user.RegistrationRequestData;
import com.redstart.server.core.message.responsedata.user.RegistrationResponseData;
import org.springframework.stereotype.Component;

@Component
public class RegistrationEventProcessor extends NoAuthSocketEventProcessor<RegistrationRequestData, RegistrationResponseData> {

    @Override
    protected RegistrationResponseData action(RegistrationRequestData data, SocketClient socketClient) {
        return new RegistrationResponseData(data.getLogin());
    }

    @Override
    public SocketEventType getEventType() {
        return SocketEventType.USER_REGISTRATION;
    }
}

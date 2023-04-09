package com.redstart.server.core.message.processor.adventure;

import com.redstart.server.core.gamemechanics.GameRoom;
import com.redstart.server.core.message.SocketEventType;
import com.redstart.server.core.message.requestdata.adventure.UpdateFrameRequestData;
import org.springframework.stereotype.Component;

@Component
public class UpdateFrameEventProcessor extends AdventureSocketEventProcessor<UpdateFrameRequestData> {
    @Override
    public SocketEventType getEventType() {
        return SocketEventType.ADVENTURE_UPDATE_FRAME;
    }

    @Override
    protected void processEvent(UpdateFrameRequestData data, GameRoom gameRoom) {

    }
}

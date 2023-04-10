package com.redstart.server.core.socket.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.redstart.server.core.socket.message.requestdata.ISocketMessageRequestData;
import com.redstart.server.core.socket.message.requestdata.adventure.*;
import com.redstart.server.core.socket.message.requestdata.user.CreateRoomRequestData;
import com.redstart.server.core.socket.message.requestdata.user.LoginRequestData;
import com.redstart.server.core.socket.message.requestdata.user.RegistrationRequestData;

public enum SocketEventType {
    @JsonProperty("leave")
    ADVENTURE_LEAVE(LeavePlayerRequestData.class),
    @JsonProperty("pause")
    ADVENTURE_PAUSE(PauseRequestData.class),
    @JsonProperty("adventureSpell")
    ADVENTURE_SPELL(SpellRequestData.class),
    @JsonProperty("adventureStep")
    ADVENTURE_STEP(StepRequestData.class),
    @JsonProperty("monsterStep")
    ADVENTURE_MONSTER_STEP(MonsterStepRequestData.class),
    @JsonProperty("updateFrame")
    ADVENTURE_UPDATE_FRAME(UpdateFrameRequestData.class),
    @JsonProperty("gameOver")
    ADVENTURE_GAME_OVER(GameOverRequestData.class),
    @JsonProperty("registration")
    USER_REGISTRATION(RegistrationRequestData.class),
    @JsonProperty("login")
    USER_LOGIN(LoginRequestData.class),
    @JsonProperty("createRoom")
    USER_CREATE_ROOM(CreateRoomRequestData.class);


    private final Class<? extends ISocketMessageRequestData> requestData;

    SocketEventType(Class<? extends ISocketMessageRequestData> requestData) {
        this.requestData = requestData;
    }

    public Class<? extends ISocketMessageRequestData> getRequestDataClass() {
        return requestData;
    }
}

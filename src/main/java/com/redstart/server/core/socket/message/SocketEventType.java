package com.redstart.server.core.socket.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.redstart.server.core.socket.message.requestdata.ISocketMessageRequestData;
import com.redstart.server.core.socket.message.requestdata.adventure.*;
import com.redstart.server.core.socket.message.requestdata.shop.UpgradeSpellRequestData;
import com.redstart.server.core.socket.message.requestdata.user.*;

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
    @JsonProperty("adventureGameOver")
    ADVENTURE_GAME_OVER(GameOverRequestData.class),
    @JsonProperty("registration")
    USER_REGISTRATION(RegistrationRequestData.class),
    @JsonProperty("login")
    USER_LOGIN(LoginRequestData.class),
    @JsonProperty("updateUserData")
    USER_UPDATE_DATA(UpdateUserRequestData.class),
    @JsonProperty("createAdventureRoom")
    USER_CREATE_ROOM(CreateRoomRequestData.class),
    @JsonProperty("adventureRoomIsCreated")
    USER_CREATED_ROOM_SUCCESS(CreateRoomSuccessRequestData.class),
    @JsonProperty("shopUpgradeSpell")
    SHOP_UPGRADE_SPELL(UpgradeSpellRequestData.class),
    @JsonProperty("leaderboard")
    LEADERBOARD(LeaderboardRequestData.class);

    private final Class<? extends ISocketMessageRequestData> requestData;

    SocketEventType(Class<? extends ISocketMessageRequestData> requestData) {
        this.requestData = requestData;
    }

    public static boolean noResponse(SocketEventType eventType) {
        switch (eventType) {
            case USER_CREATED_ROOM_SUCCESS:
            case ADVENTURE_LEAVE:
                return true;
            default:
                return false;
        }
    }

    public Class<? extends ISocketMessageRequestData> getRequestDataClass() {
        return requestData;
    }

    public boolean isAdventureType() {
        switch (this) {
            case ADVENTURE_LEAVE:
            case ADVENTURE_PAUSE:
            case ADVENTURE_SPELL:
            case ADVENTURE_STEP:
            case ADVENTURE_MONSTER_STEP:
            case ADVENTURE_UPDATE_FRAME:
            case ADVENTURE_GAME_OVER:
                return true;
            default:
                return false;
        }
    }
}

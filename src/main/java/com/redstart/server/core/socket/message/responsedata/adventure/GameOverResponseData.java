package com.redstart.server.core.socket.message.responsedata.adventure;

import com.redstart.server.core.socket.message.responsedata.ISocketMessageResponseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GameOverResponseData implements ISocketMessageResponseData {
    private PlayerGameOver player;
    public static GameOverResponseData of(int earnedMoney, int earnedExperience) {
        GameOverResponseData data = new GameOverResponseData();
        PlayerGameOver player = new PlayerGameOver();
        player.setEarnedMoney(earnedMoney);
        player.setEarnedExperience(earnedExperience);
        data.setPlayer(player);
        return data;
    }

    @Data
    @NoArgsConstructor
    private static class PlayerGameOver {
        private int earnedMoney;
        private int earnedExperience;
    }
}

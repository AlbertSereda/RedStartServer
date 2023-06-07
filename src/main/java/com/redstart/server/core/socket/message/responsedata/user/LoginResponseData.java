package com.redstart.server.core.socket.message.responsedata.user;

import com.redstart.server.core.dto.*;
import com.redstart.server.core.socket.message.responsedata.ErrorResponse;
import com.redstart.server.core.socket.message.responsedata.ISocketMessageResponseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseData implements ISocketMessageResponseData {
    private String login;
    private int level;
    private int money;
    private String message;
    private int experience;
    //Ниже новое:
    private List<SpellForUserDTO> spellsForUser; // заклинания юзера
    private List<LevelCompletedDTO> levelsCompleted; // завершенные уровни

    //Общая информация:
    private List<IslandDTO> islands; // все острова


    public static LoginResponseData ofSuccess(UserDTO user,
                                              List<IslandDTO> islands,
                                              String message) {
        LoginResponseData data = new LoginResponseData();
        data.setLogin(user.getLogin());
        data.setLevel(user.getLevel());
        data.setMoney(user.getMoney());
        data.setMessage(message);
        data.setExperience(user.getExperience());

        data.setSpellsForUser(user.getSpellsForUser());
        data.setLevelsCompleted(user.getLevelsCompleted());
        data.setIslands(islands);
        return data;
    }
}

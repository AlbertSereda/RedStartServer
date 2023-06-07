package com.redstart.server.core.socket.message.responsedata.user;

import com.redstart.server.core.dto.UserDTO;
import com.redstart.server.core.socket.message.responsedata.ErrorResponse;
import com.redstart.server.core.socket.message.responsedata.ISocketMessageResponseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Deprecated
public class RegistrationResponseData implements ISocketMessageResponseData {
    private String login;
    private int level;
    private int money;
    private String message;
    private ErrorResponse error;

    public static RegistrationResponseData ofSuccess(UserDTO user) {
        RegistrationResponseData data = new RegistrationResponseData();
        data.setLogin(user.getLogin());
        data.setLevel(user.getLevel());
        data.setMoney(user.getMoney());
        data.setMessage("registration success!");
        return data;
    }

    public static RegistrationResponseData ofError(ErrorResponse error) {
        RegistrationResponseData data = new RegistrationResponseData();
        data.setError(error);
        return data;
    }
}

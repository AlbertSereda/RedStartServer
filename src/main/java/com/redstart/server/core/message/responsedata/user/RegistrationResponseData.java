package com.redstart.server.core.message.responsedata.user;

import com.redstart.server.core.message.responsedata.ISocketMessageResponseData;

public class RegistrationResponseData implements ISocketMessageResponseData {
    private String login;

    public RegistrationResponseData() {
    }

    public RegistrationResponseData(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}

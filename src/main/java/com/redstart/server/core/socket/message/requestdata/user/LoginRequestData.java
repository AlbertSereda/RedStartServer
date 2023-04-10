package com.redstart.server.core.socket.message.requestdata.user;

import com.redstart.server.core.socket.message.requestdata.ISocketMessageRequestData;

public class LoginRequestData implements ISocketMessageRequestData {
    private String login;
    private String password;

    public LoginRequestData() {
    }

    public LoginRequestData(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

package com.redstart.server.core.message.requestdata.user;

import com.redstart.server.core.message.requestdata.ISocketMessageRequestData;

public class RegistrationRequestData implements ISocketMessageRequestData {
    private String login;
    private String password;
    private String email;

    public RegistrationRequestData() {
    }

    public RegistrationRequestData(String login, String password, String email) {
        this.login = login;
        this.password = password;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

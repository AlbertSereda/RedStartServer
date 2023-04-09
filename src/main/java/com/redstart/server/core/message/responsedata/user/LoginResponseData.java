package com.redstart.server.core.message.responsedata.user;

import com.redstart.server.core.message.responsedata.ISocketMessageResponseData;

public class LoginResponseData implements ISocketMessageResponseData {
    private String login;
    private int level;
    private int money;
    private String message;

    public LoginResponseData() {
    }

    public LoginResponseData(String login, int level, int money, String message) {
        this.login = login;
        this.level = level;
        this.money = money;
        this.message = message;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}

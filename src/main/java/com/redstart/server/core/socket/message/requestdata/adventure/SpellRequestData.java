package com.redstart.server.core.socket.message.requestdata.adventure;

import com.redstart.server.core.socket.message.requestdata.ISocketMessageRequestData;

public class SpellRequestData implements ISocketMessageRequestData {
    private String nameSpell;

    public SpellRequestData() {
    }

    public SpellRequestData(String nameSpell) {
        this.nameSpell = nameSpell;
    }

    public String getNameSpell() {
        return nameSpell;
    }

    public void setNameSpell(String nameSpell) {
        this.nameSpell = nameSpell;
    }
}

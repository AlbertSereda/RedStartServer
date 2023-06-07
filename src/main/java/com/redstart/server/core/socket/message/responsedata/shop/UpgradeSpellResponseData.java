package com.redstart.server.core.socket.message.responsedata.shop;

import com.redstart.server.core.dto.SpellForUserDTO;
import com.redstart.server.core.socket.message.responsedata.ISocketMessageResponseData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpgradeSpellResponseData implements ISocketMessageResponseData {
    private List<SpellForUserDTO> spellsForUser;
    private int money;

    public static UpgradeSpellResponseData of(List<SpellForUserDTO> spellsForUser, int money) {
        UpgradeSpellResponseData data = new UpgradeSpellResponseData();
        data.setSpellsForUser(spellsForUser);
        data.setMoney(money);
        return data;
    }
}

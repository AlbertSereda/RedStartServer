package com.redstart.server.core.socket.message.requestdata.shop;

import com.redstart.server.core.socket.message.requestdata.ISocketMessageRequestData;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpgradeSpellRequestData implements ISocketMessageRequestData {
    private String nameSpell;
}

package com.redstart.server.core.socket.message.processor.shop;

import com.redstart.server.core.dto.SpellForUserDTO;
import com.redstart.server.core.dto.UserDTO;
import com.redstart.server.core.socket.SocketClient;
import com.redstart.server.core.socket.message.SocketEventType;
import com.redstart.server.core.socket.message.processor.AuthSocketEventProcessor;
import com.redstart.server.core.socket.message.requestdata.shop.UpgradeSpellRequestData;
import com.redstart.server.core.socket.message.responsedata.ErrorResponse;
import com.redstart.server.core.socket.message.responsedata.shop.UpgradeSpellResponseData;
import com.redstart.server.database.service.SpellForUserService;
import com.redstart.server.database.service.UserDataService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UpgradeSpellProcessor extends AuthSocketEventProcessor<UpgradeSpellRequestData, UpgradeSpellResponseData> {
    private final SpellForUserService spellForUserService;
    private final UserDataService userDataService;
    private final int costUpgradeSpell;

    public UpgradeSpellProcessor(SpellForUserService spellForUserService,
                                 UserDataService userDataService,
                                 @Value("${shop.costUpgradeSpell}") int costUpgradeSpell) {
        this.spellForUserService = spellForUserService;
        this.userDataService = userDataService;
        this.costUpgradeSpell = costUpgradeSpell;
    }

    @Override
    protected UpgradeSpellResponseData action(UpgradeSpellRequestData data, SocketClient socketClient) {
        String login = socketClient.getLogin();
        String nameUpgradeSpell = data.getNameSpell();

        UserDTO userdata = userDataService.getUserdata(login);
        int userMoney = userdata.getMoney();

        if (userMoney >= costUpgradeSpell) {
            spellForUserService.updateUserSpell(login, nameUpgradeSpell);
            userdata.setMoney(userdata.getMoney() - costUpgradeSpell);
            userDataService.updateUserMoney(userdata);
            List<SpellForUserDTO> allSpellForUser = spellForUserService.getAllSpellForUser(login);
            userdata.setSpellsForUser(allSpellForUser);

        } else {
            throw new IllegalStateException(ErrorResponse.NOT_ENOUGH_MONEY.getErrorMessage());
        }

        List<SpellForUserDTO> spellsForUser = userdata.getSpellsForUser();
        return UpgradeSpellResponseData.of(spellsForUser, userdata.getMoney());
    }

    @Override
    public SocketEventType getEventType() {
        return SocketEventType.SHOP_UPGRADE_SPELL;
    }
}

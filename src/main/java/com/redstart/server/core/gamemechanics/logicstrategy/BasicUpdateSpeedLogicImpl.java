package com.redstart.server.core.gamemechanics.logicstrategy;

import com.redstart.server.core.gamemechanics.logicstrategy.interfaces.UpdateSpeedLogic;

public class BasicUpdateSpeedLogicImpl implements UpdateSpeedLogic {

    public int updateCurrentSpeed(long maxSpeed, long timeCreation) {
        long timePassed = System.currentTimeMillis() - timeCreation;
        return (int) (maxSpeed - timePassed);
    }
}

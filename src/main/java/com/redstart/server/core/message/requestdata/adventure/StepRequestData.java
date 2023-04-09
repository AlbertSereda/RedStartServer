package com.redstart.server.core.message.requestdata.adventure;

import com.redstart.server.core.message.requestdata.ISocketMessageRequestData;

public class StepRequestData implements ISocketMessageRequestData {
    int nameBlockDestroyed;

    public StepRequestData() {
    }

    public StepRequestData(int nameBlockDestroyed) {
        this.nameBlockDestroyed = nameBlockDestroyed;
    }

    public int getNameBlockDestroyed() {
        return nameBlockDestroyed;
    }

    public void setNameBlockDestroyed(int nameBlockDestroyed) {
        this.nameBlockDestroyed = nameBlockDestroyed;
    }
}

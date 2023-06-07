package com.redstart.server.core.socket.message.requestdata.user;

import com.redstart.server.core.socket.message.requestdata.ISocketMessageRequestData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoomRequestData implements ISocketMessageRequestData {
    private String islandName;
    private int levelNumber;
    private Set<String> userSelectedSpells;
}

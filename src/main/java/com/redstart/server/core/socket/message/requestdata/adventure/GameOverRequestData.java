package com.redstart.server.core.socket.message.requestdata.adventure;

import com.redstart.server.core.gamemechanics.GameRoom;
import com.redstart.server.core.socket.SocketClient;
import com.redstart.server.core.socket.message.requestdata.ISocketMessageRequestData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameOverRequestData implements ISocketMessageRequestData {
    private GameRoom gameRoom;
}

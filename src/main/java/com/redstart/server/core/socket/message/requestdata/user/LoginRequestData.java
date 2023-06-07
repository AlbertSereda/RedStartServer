package com.redstart.server.core.socket.message.requestdata.user;

import com.redstart.server.core.socket.message.requestdata.ISocketMessageRequestData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestData implements ISocketMessageRequestData {
    private String login;
    private String password;
}

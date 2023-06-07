package com.redstart.server.core.socket.message.responsedata.user;

import com.redstart.server.core.dto.LeaderDTO;
import com.redstart.server.core.socket.message.responsedata.ISocketMessageResponseData;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class LeaderboardResponseData implements ISocketMessageResponseData {
    private List<LeaderDTO> leaderDTOList;

    public static LeaderboardResponseData of(List<LeaderDTO> leaderDTOList) {
        LeaderboardResponseData data = new LeaderboardResponseData();
        data.setLeaderDTOList(leaderDTOList);
        return data;
    }
}

package com.redstart.server.core.socket.message.processor.user;

import com.redstart.server.core.dto.LeaderDTO;
import com.redstart.server.core.socket.SocketClient;
import com.redstart.server.core.socket.message.SocketEventType;
import com.redstart.server.core.socket.message.processor.AuthSocketEventProcessor;
import com.redstart.server.core.socket.message.requestdata.user.LeaderboardRequestData;
import com.redstart.server.core.socket.message.responsedata.user.LeaderboardResponseData;
import com.redstart.server.database.service.LeaderboardService;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class LeaderboardEventProcessor extends AuthSocketEventProcessor<LeaderboardRequestData, LeaderboardResponseData> {
    private final LeaderboardService leaderboardService;

    public LeaderboardEventProcessor(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    @Override
    protected LeaderboardResponseData action(LeaderboardRequestData data, SocketClient socketClient) {
        List<LeaderDTO> leaderDTOList = leaderboardService.getLeaderboard();
        return LeaderboardResponseData.of(leaderDTOList);
    }

    @Override
    public SocketEventType getEventType() {
        return SocketEventType.LEADERBOARD;
    }
}

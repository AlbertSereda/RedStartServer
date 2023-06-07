package com.redstart.server.database.service;

import com.redstart.server.core.dto.LeaderDTO;

import java.util.List;

public interface LeaderboardService {
    List<LeaderDTO> getLeaderboard();
}

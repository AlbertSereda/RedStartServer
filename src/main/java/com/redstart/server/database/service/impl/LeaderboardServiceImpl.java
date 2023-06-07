package com.redstart.server.database.service.impl;

import com.redstart.server.core.dto.LeaderDTO;
import com.redstart.server.database.entity.UserDataEntity;
import com.redstart.server.database.service.LeaderboardService;
import com.redstart.server.database.service.UserDataService;
import org.springframework.stereotype.Service;
import org.springframework.util.comparator.Comparators;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaderboardServiceImpl implements LeaderboardService {
    private final UserDataService userDataService;

    public LeaderboardServiceImpl(UserDataService userDataService) {
        this.userDataService = userDataService;
    }

    @Override
    public List<LeaderDTO> getLeaderboard() {
        List<UserDataEntity> userLeaders = userDataService.getUsersDataToLeaderboard();
        return userLeaders
                .stream()
                .map(LeaderDTO::of)
                .sorted(Comparator.comparingInt(LeaderDTO::getLevel)
                        .reversed()
                        .thenComparing(LeaderDTO::getLogin))
                .collect(Collectors.toList());
    }
}

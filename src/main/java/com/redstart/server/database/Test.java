package com.redstart.server.database;

import com.redstart.server.core.dto.LeaderDTO;
import com.redstart.server.core.socket.message.processor.user.LeaderboardEventProcessor;
import com.redstart.server.core.socket.message.processor.user.RegistrationEventProcessor;
import com.redstart.server.database.service.*;
import org.springframework.stereotype.Component;

import java.util.List;

//@Component
public class Test implements Runnable {
    private final IslandCompletedService islandCompletedService;
    private final IslandService islandService;
    private final LevelCompletedService levelCompletedService;
    private final LevelService levelService;
    private final MonsterService monsterService;
    private final SpellDefaultService spellDefaultService;
    private final SpellForUserService spellForUserService;
    private final UserDataService userDataService;
    private final UserService userService;
    private final RegistrationEventProcessor registrationEventProcessor;
    private final LeaderboardEventProcessor leaderboardEventProcessor;
    private final LeaderboardService leaderboardService;

    public Test(IslandCompletedService islandCompletedService,
                IslandService islandService,
                LevelCompletedService levelCompletedService,
                LevelService levelService,
                MonsterService monsterService,
                SpellDefaultService spellDefaultService,
                SpellForUserService spellForUserService,
                UserDataService userDataService,
                UserService userService,
                RegistrationEventProcessor registrationEventProcessor,
                LeaderboardEventProcessor leaderboardEventProcessor, LeaderboardService leaderboardService) {
        this.islandCompletedService = islandCompletedService;
        this.islandService = islandService;
        this.levelCompletedService = levelCompletedService;
        this.levelService = levelService;
        this.monsterService = monsterService;
        this.spellDefaultService = spellDefaultService;
        this.spellForUserService = spellForUserService;
        this.userDataService = userDataService;
        this.userService = userService;
        this.registrationEventProcessor = registrationEventProcessor;
        this.leaderboardEventProcessor = leaderboardEventProcessor;
        this.leaderboardService = leaderboardService;
        new Thread(this).start();
    }


    @Override
    public void run() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        leaderboardService.getLeaderboard()
                .forEach(leaderDTO -> {
                    System.out.println(leaderDTO);
                });

//      Получение всей цепочки Острова
//        Optional<IslandEntity> maybeFireIsland = islandService.findById("fire_island");
//        if (maybeFireIsland.isPresent()) {
//            IslandEntity islandsEntity = maybeFireIsland.get();
//            System.out.println("Island : " + islandsEntity);
//        }
//        -------------------------------

//      Получение всей цепочки Юзера
//        Optional<UserEntity> maybeIgnoreCase1 = userService.findByLoginIgnoreCase("Albert");
//        if (maybeIgnoreCase1.isPresent()) {
//            UserEntity userEntity = maybeIgnoreCase1.get();
//            System.out.println("Albert : " + userEntity);
//        }
//
//        Optional<UserEntity> maybeIgnoreCase2 = userService.findByLoginIgnoreCase("albert");
//        if (maybeIgnoreCase2.isPresent()) {
//            UserEntity userEntity = maybeIgnoreCase2.get();
//            System.out.println("albert : " + userEntity);
//        }
//        -------------------------------


//        Завершенные острова
//        List<IslandCompletedEntity> all = islandCompletedService.findAll();
//        System.out.println("all : " + all);
//
//        Optional<IslandCompletedEntity> maybeIsland = islandCompletedService.findById(1);
//        if (maybeIsland.isPresent()) {
//            IslandCompletedEntity islandsCompletedEntity = maybeIsland.get();
//            System.out.println("by id 1 : " + islandsCompletedEntity);
//        }
//        -------------------------------

    }
}

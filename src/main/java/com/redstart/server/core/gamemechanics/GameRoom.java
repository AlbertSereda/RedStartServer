package com.redstart.server.core.gamemechanics;

import com.redstart.server.core.dto.*;
import com.redstart.server.core.gamemechanics.logicstrategy.BasicMonsterMoveLogicImpl;
import com.redstart.server.core.gamemechanics.logicstrategy.BasicUpdateSpeedLogicImpl;
import com.redstart.server.core.gamemechanics.spells.ArrowSpell;
import com.redstart.server.core.gamemechanics.spells.FireBallSpell;
import com.redstart.server.core.gamemechanics.spells.NextDamageProtectionSpell;
import com.redstart.server.core.gamemechanics.spells.StanSpell;
import com.redstart.server.core.gamemechanics.spells.interfaces.ISpell;
import com.redstart.server.core.gamemechanics.spells.interfaces.WithTimeSpell;
import com.redstart.server.core.socket.jsonclasses.Monster;
import com.redstart.server.core.socket.jsonclasses.Player;
import com.redstart.server.core.socket.message.responsedata.adventure.AdventureResponseData;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GameRoom {
    private Player player;
    private Monster monster;

    private final Lock lock;

    private GameLogic gameLogic;

    private final AdventureResponseData adventureData;

    private AtomicBoolean isGameOver;

    private long timeSetPause;

    private long lastAddToBuffer;

    private final LevelDTO levelDTO;
    private final String nameIsland;
    private final UserDTO userDTO;
    private LocalDateTime startGame = LocalDateTime.now();
    private LocalDateTime endGame;
    private int earnedExperience = 0;

    public GameRoom(GameLogic gameLogic,
                    String login,
                    List<ISpell> selectedSpells,
                    MonsterDTO monsterDTO,
                    LevelDTO levelDTO,
                    String nameIsland,
                    UserDTO userDTO) {
        isGameOver = new AtomicBoolean(false);
        player = new Player(login, 100, 0, 0);

        selectedSpells.forEach(iSpell -> {
            iSpell.setGameRoom(this);
            player.addAvailableSpell(iSpell);
        });

//        player.addAvailableSpell(new FireBallSpell(this,
//                20,
//                20));
//        player.addAvailableSpell(new StanSpell(this,
//                20,
//                5000,
//                0));
//        player.addAvailableSpell(new NextDamageProtectionSpell(this,
//                20,
//                0));
//
//        player.addAvailableSpell(new ArrowSpell(this,
//                10,
//                10));

        monster = new Monster(
                monsterDTO.getName(),
                monsterDTO.getHp(),
                monsterDTO.getSpeed(),
                new BasicMonsterMoveLogicImpl(this),
                new BasicUpdateSpeedLogicImpl());

        lock = new ReentrantLock();
        this.gameLogic = gameLogic;
        gameLogic.fillFieldForServer(player);
        adventureData = new AdventureResponseData(player, monster, GameState.CREATING);

        this.levelDTO = levelDTO;
        this.nameIsland = nameIsland;
        this.userDTO = userDTO;

        lastAddToBuffer = System.currentTimeMillis();
    }

    public void setPause() {
        if (adventureData.getGameState() == GameState.RESUME) {
            adventureData.setGameState(GameState.PAUSE);
            timeSetPause = System.currentTimeMillis();
            LocalDateTime setPauseTime = LocalDateTime.now();
            earnedExperience += ChronoUnit.SECONDS.between(startGame, setPauseTime);
        } else if (adventureData.getGameState() == GameState.PAUSE) {
            long timePassedSetPause = System.currentTimeMillis() - timeSetPause;
            monster.setTimeCreation(monster.getTimeCreation() + timePassedSetPause);
            List<WithTimeSpell> activeSpells = player.getActiveSpellForServer();
            for (WithTimeSpell spell : activeSpells) {
                spell.setTimeCreation(spell.getTimeCreation() + timePassedSetPause);
            }
            timeSetPause = 0;
            adventureData.setGameState(GameState.RESUME);
            startGame = LocalDateTime.now();
        }
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Monster getMonster() {
        return monster;
    }

    public void setMonster(Monster monster) {
        this.monster = monster;
    }

    public GameLogic getGameLogic() {
        return gameLogic;
    }

    public void setGameLogic(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
    }

    public AdventureResponseData getAdventureData() {
        return adventureData;
    }

    public Lock getLock() {
        return lock;
    }

    public long getTimeSetPause() {
        return timeSetPause;
    }

    public void setTimeSetPause(long timeSetPause) {
        this.timeSetPause = timeSetPause;
    }

    public AtomicBoolean getIsGameOver() {
        return isGameOver;
    }

    public void setIsGameOver(AtomicBoolean isGameOver) {
        this.isGameOver = isGameOver;
    }

    public long getLastAddToBuffer() {
        return lastAddToBuffer;
    }

    public void setLastAddToBuffer(long lastAddToBuffer) {
        this.lastAddToBuffer = lastAddToBuffer;
    }

    public LevelDTO getLevelDTO() {
        return levelDTO;
    }

    public String getNameIsland() {
        return nameIsland;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public LocalDateTime getStartGame() {
        return startGame;
    }

    public void setStartGame(LocalDateTime startGame) {
        this.startGame = startGame;
    }

    public int getEarnedExperience() {
        return earnedExperience;
    }

    public void setEarnedExperience(int earnedExperience) {
        this.earnedExperience = earnedExperience;
    }

    public LocalDateTime getEndGame() {
        return endGame;
    }

    public void setEndGame(LocalDateTime endGame) {
        this.endGame = endGame;
    }
}

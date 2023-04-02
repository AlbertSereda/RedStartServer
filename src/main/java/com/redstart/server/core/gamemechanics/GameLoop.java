package com.redstart.server.core.gamemechanics;

import com.redstart.server.core.SocketClient;
import com.redstart.server.core.SocketHandler;
import com.redstart.server.core.gamemechanics.spells.interfaces.WithTimeSpell;
import com.redstart.server.core.jsonclasses.Monster;
import com.redstart.server.core.jsonclasses.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;

@Component
public class GameLoop implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(GameLoop.class);
    public static final int FRAME_SERVER = 30;

    private final Map<SocketClient, GameRoom> gameRooms;

    private final GameRoomExecutor gameRoomExecutor;
    private GameLogicExecutor gameLogicExecutor;

    private final GameLogic gameLogic;

    private final SocketHandler socketHandler;

    public GameLoop(GameRoomExecutor gameRoomExecutor,
                    GameLogicExecutor gameLogicExecutor,
                    GameLogic gameLogic,
                    SocketHandler socketHandler) {
        this.gameRoomExecutor = gameRoomExecutor;
        this.gameRooms = gameRoomExecutor.getGameRooms();
        this.gameLogicExecutor = gameLogicExecutor;
        this.gameLogic = gameLogic;
        this.socketHandler = socketHandler;
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (true) {
            for (Map.Entry<SocketClient, GameRoom> entry : gameRooms.entrySet()) {
                SocketClient socketClient = entry.getKey();
                GameRoom gameRoom = entry.getValue();

                if (gameRoom.getIsGameOver().get()) {
                    continue;
                }

                Monster monster = gameRoom.getMonster();
                GameState gameState = gameRoom.getAdventureData().getGameState();

                List<WithTimeSpell> activeSpells = gameRoom.getPlayer().getActiveSpellForServer();

                switch (gameState) {
                    case WIN:
                    case LOSE:
                        clearActiveSpell(gameRoom);
                        break;
                    case RESUME:
                        int fixSize = 0;
                        for (int i = 0; i < activeSpells.size() - fixSize; i++) {
                            WithTimeSpell spell = activeSpells.get(i);
                            spell.update();
                            if (spell.getTimeToEnd() <= 0) {
                                spell.deactivate();
                                gameRoom.getPlayer().removeActiveSpell(activeSpells.get(i));
                                fixSize++;
                            }
                        }

                        int currentSpeed = monster.getUpdateSpeedLogic().updateCurrentSpeed(monster.getMaxSpeed(), monster.getTimeCreation());
                        monster.setCurrentSpeed(currentSpeed);
                        if (monster.getCurrentSpeed() <= 0) {
                            gameLogicExecutor.executeMove(socketClient, Move.MONSTER);
                            monster.setNewTimeCreation();
                        }

                        long now = System.currentTimeMillis();
                        if ((now - gameRoom.getLastAddToBuffer()) >= FRAME_SERVER) {
                            Lock lock = gameRoom.getLock();
                            if (lock.tryLock()) {
                                try {
                                    socketHandler.addToQueueObject(socketClient, gameRoom);
                                    gameRoom.setLastAddToBuffer(now);
                                } finally {
                                    lock.unlock();
                                }
                            }
                        }
                        break;
                }
                boolean isGameOver = gameLogic.checkGameOver(socketClient, gameRoom);
                if (isGameOver) {
                    gameOver(socketClient, gameRoom);
                }
            }
        }
    }

    public void gameOver(SocketClient socketClient, GameRoom gameRoom) {
        gameRoom.getIsGameOver().set(true);
        gameLogicExecutor.executeGameOver(socketClient, gameRoom);
    }

    public void setGameLogicExecutor(GameLogicExecutor gameLogicExecutor) {
        this.gameLogicExecutor = gameLogicExecutor;
    }

    private void clearActiveSpell(GameRoom gameRoom) {
        Player player = gameRoom.getPlayer();
        player.getActiveSpellForServer().clear();
        //player.getActiveSpells().clear();
    }
}
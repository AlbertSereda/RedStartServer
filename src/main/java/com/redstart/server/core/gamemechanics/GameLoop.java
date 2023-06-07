package com.redstart.server.core.gamemechanics;

import com.redstart.server.core.gamemechanics.spells.interfaces.WithTimeSpell;
import com.redstart.server.core.socket.SocketClient;
import com.redstart.server.core.socket.jsonclasses.Monster;
import com.redstart.server.core.socket.jsonclasses.Player;
import com.redstart.server.core.socket.message.SocketDataUpdater;
import com.redstart.server.core.socket.message.SocketEventType;
import com.redstart.server.core.socket.message.requestdata.adventure.GameOverRequestData;
import com.redstart.server.core.socket.message.requestdata.adventure.StepRequestData;
import com.redstart.server.core.socket.message.responsedata.adventure.AdventureResponseData;
import com.redstart.server.core.socket.repository.GameRoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;

@Component
public class GameLoop implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(GameLoop.class);
    public final int frameServer;

    private final Map<SocketClient, GameRoom> gameRooms;

    private final GameLogic gameLogic;

    private final SocketDataUpdater dataUpdater;

    public GameLoop(GameRoomRepository gameRoomRepository,
                    GameLogic gameLogic,
                    SocketDataUpdater dataUpdater,
                    @Value("${adventure.frameServer}") int frameServer) {
        this.gameRooms = gameRoomRepository.getGameRooms();
        this.gameLogic = gameLogic;
        this.dataUpdater = dataUpdater;
        this.frameServer = frameServer;
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

                GameState gameState = gameRoom.getAdventureData().getGameState();

                switch (gameState) {
                    case LEAVE:
                        dataUpdater.updateFrame(SocketEventType.ADVENTURE_UPDATE_FRAME, socketClient, AdventureResponseData.of(gameRoom));
                        gameOver(socketClient, gameRoom, false);
                        break;
                    case CREATING:
                        if (!socketClient.getSocketChannel().isConnected()) {
                            gameOver(socketClient, gameRoom, false);
                        }
                        break;
                    case WIN:
                    case LOSE:
                        clearActiveSpell(gameRoom);
                        break;
                    case RESUME:
//                        int fixSize = 0;
//                        for (int i = 0; i < activeSpells.size() - fixSize; i++) {
//                            WithTimeSpell spell = activeSpells.get(i);
//                            spell.update();
//                            if (spell.getTimeToEnd() <= 0) {
//                                spell.deactivate();
//                                gameRoom.getPlayer().removeActiveSpell(activeSpells.get(i));
//                                fixSize++;
//                            }
//                        }
                        Player player = gameRoom.getPlayer();
                        List<WithTimeSpell> activeSpells = player.getActiveSpellForServer();

                        activeSpells.stream().forEach(spell -> {
                            spell.update();
                            if (spell.getTimeToEnd() <= 0) {
                                spell.deactivate();
                                player.removeActiveSpell(spell);
                            }
                        });

                        Monster monster = gameRoom.getMonster();
                        int currentSpeed = monster.getUpdateSpeedLogic().updateCurrentSpeed(monster.getMaxSpeed(), monster.getTimeCreation());
                        monster.setCurrentSpeed(currentSpeed);
                        if (monster.getCurrentSpeed() <= 0) {
                            //gameLogicExecutor.executeMove(socketClient, Move.MONSTER);
                            dataUpdater.updateAdventureData(SocketEventType.ADVENTURE_MONSTER_STEP, new StepRequestData(), socketClient);
                            monster.setNewTimeCreation();
                        }

                        long now = System.currentTimeMillis();
                        if ((now - gameRoom.getLastAddToBuffer()) >= frameServer) {
                            Lock lock = gameRoom.getLock();
                            if (lock.tryLock()) {
                                try {
                                    //socketHandler.addToWriteObject(socketClient, gameRoom);
                                    dataUpdater.updateFrame(SocketEventType.ADVENTURE_UPDATE_FRAME, socketClient, AdventureResponseData.of(gameRoom));
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
                    log.info("Game over {}, {}", gameRoom.getPlayer().getName(), gameRoom.getAdventureData().getGameState());
                    dataUpdater.updateFrame(SocketEventType.ADVENTURE_UPDATE_FRAME, socketClient, AdventureResponseData.of(gameRoom));
                    gameOver(socketClient, gameRoom, true);
                }
            }
        }
    }

    public void gameOver(SocketClient socketClient, GameRoom gameRoom, boolean isNeedSendToClient) {
        gameRoom.getIsGameOver().set(true);
        dataUpdater.updateUserData(SocketEventType.ADVENTURE_GAME_OVER, new GameOverRequestData(gameRoom), socketClient, isNeedSendToClient);
    }

    private void clearActiveSpell(GameRoom gameRoom) {
        Player player = gameRoom.getPlayer();
        player.getActiveSpellForServer().clear();
        //player.getActiveSpells().clear();
    }
}
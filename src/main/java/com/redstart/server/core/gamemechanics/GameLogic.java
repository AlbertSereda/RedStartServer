package com.redstart.server.core.gamemechanics;

import com.redstart.server.core.SocketClient;
import com.redstart.server.core.gamemechanics.block.ColorBlock;
import com.redstart.server.core.gamemechanics.spells.StanSpell;
import com.redstart.server.core.gamemechanics.spells.interfaces.Spell;
import com.redstart.server.core.jsonclasses.Monster;
import com.redstart.server.core.jsonclasses.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class GameLogic {
    private static final Logger log = LoggerFactory.getLogger(GameLogic.class);

    private static final int LENGTH_FIELD = 9;
    private static final int COUNT_COLOR = 4;

    private static final int INDEX_NAME = 0;
    private static final int INDEX_CLIENT = 1;
    private static final int INDEX_COLOR = 2;

    private final Map<Integer, ColorBlock> colorBlocks;


    public GameLogic(Set<ColorBlock> colorBlocks) {
        this.colorBlocks = colorBlocks.stream().collect(Collectors.toMap(ColorBlock::getNumberBlock, colorBlock -> colorBlock));
        //colorBlocks = new ColorBlockInitializer().getColorBlocks();
    }

    public void fillFieldForServer(Player player) {
        int[][][] fieldForServer = new int[LENGTH_FIELD][LENGTH_FIELD][];

        int nextNameBlock = player.getNextNameBlock();
        int nextIndexBlock = 0;
        for (int i = fieldForServer.length - 1; i >= 0; i--) {
            for (int j = 0; j < fieldForServer[i].length; j++) {

                int color = getRandomColor();
                //int color = 1;
                int[] block = new int[]{nextNameBlock, nextIndexBlock, color};
                nextIndexBlock++;
                fieldForServer[i][j] = block;
                player.getSpawnedBlocks().add(block);
                nextNameBlock++;
            }
        }
        player.setFieldForServer(fieldForServer);
        player.setNextNameBlock(nextNameBlock);
        //testing(fieldForServer);
    }

    public static void testing(int[][][] fieldForServer) {
        for (int i = 0; i < fieldForServer.length; i++) {
            for (int j = 0; j < fieldForServer[i].length; j++) {
                System.out.print(fieldForServer[i][j][INDEX_COLOR] + " ");

            }
            for (int j = 0; j < fieldForServer[i].length; j++) {
                System.out.printf("%4d", fieldForServer[i][j][INDEX_NAME]);
            }
            System.out.print(" ");
            for (int j = 0; j < fieldForServer[i].length; j++) {
                System.out.printf("%3d", fieldForServer[i][j][INDEX_CLIENT]);
            }


            System.out.println();
        }
    }

    private int getRandomColor() {
        return (int) (Math.random() * COUNT_COLOR);
    }


    public void playerMove(GameRoom gameRoom, int nameBlockDestroyed) {
        Player player = gameRoom.getPlayer();
        List<Integer> blastedBlocks = player.getBlastedBlocks();
        List<int[]> spawnedBlocks = player.getSpawnedBlocks();

        blastedBlocks.clear();
        spawnedBlocks.clear();

        int[][][] fieldForServer = player.getFieldForServer();

        int chooseColor = 0;
        boolean isBreak = false;
        for (int i = 0; i < fieldForServer.length; i++) {
            if (isBreak) break;
            for (int j = 0; j < fieldForServer[i].length; j++) {
                if (fieldForServer[i][j][INDEX_NAME] == nameBlockDestroyed) {
                    blastedBlocks.add(fieldForServer[i][j][INDEX_NAME]);
                    chooseColor = fieldForServer[i][j][INDEX_COLOR];
                    actionChooseCell(player, j, i);
                    isBreak = true;
                    break;
                }
            }
        }

        ColorBlock colorBlock = colorBlocks.get(chooseColor);
        colorBlock.executeAction(gameRoom);

        //testing(fieldForServer);
    }

    public void decrementPlayerHp(Player player, int countDamage) {
        int playerHp = player.getHp();
        int playerShield = player.getShield();

        int damageToHp = countDamage / 2;
        int damageToShield = countDamage - damageToHp;

        //log.info("Damage to player - " + countDamage);

        playerShield -= damageToShield;
        if (playerShield < 0) {
            damageToHp += playerShield * (-1);
            playerShield = 0;
        }
        playerHp -= damageToHp;

        if (playerHp < 1) {
            playerHp = 0;
        }

        player.setHp(playerHp);
        player.setShield(playerShield);
    }

    public void decrementMonsterHp(Monster monster, int countDamage) {
        int monsterHP = monster.getHp() - countDamage;
        if (monsterHP < 1) {
            monsterHP = 0;
        }
        monster.setHp(monsterHP);
    }

    private void actionChooseCell(Player player, int x, int y) {

        int[][][] fieldForServer = player.getFieldForServer();

        int chooseColor = fieldForServer[y][x][INDEX_COLOR];
        fieldForServer[y][x][INDEX_COLOR] = -1;
        int countChange = 1;
        int countCheck = 0;

        while (countChange != countCheck) {
            for (int i = 0; i < fieldForServer.length; i++) {
                for (int j = 0; j < fieldForServer[i].length; j++) {
                    if (fieldForServer[i][j][INDEX_COLOR] == -1) {
                        countChange += changeAroundCell(player, j, i, chooseColor);
                        fieldForServer[i][j][INDEX_COLOR] = -2;
                        countCheck++;
                    }
                }
            }
        }

        moveDestroyedCellsToTop(fieldForServer);
        fillingEmptyCells(player);
    }

    private int changeAroundCell(Player player, int x, int y, int chooseColor) {

        int[][][] fieldForServer = player.getFieldForServer();
        List<Integer> blastedBlocks = player.getBlastedBlocks();

        int countChange = 0;
        int ky = -1;
        int kx = 0;
        if (y + ky >= 0 && fieldForServer[y + ky][x + kx][INDEX_COLOR] == chooseColor) {
            fieldForServer[y + ky][x + kx][INDEX_COLOR] = -1;
            countChange++;
            blastedBlocks.add(fieldForServer[y + ky][x + kx][INDEX_NAME]);
        }

        ky = 1;
        if (y + ky <= fieldForServer.length - 1 && fieldForServer[y + ky][x + kx][INDEX_COLOR] == chooseColor) {
            fieldForServer[y + ky][x + kx][INDEX_COLOR] = -1;
            countChange++;
            blastedBlocks.add(fieldForServer[y + ky][x + kx][INDEX_NAME]);
        }

        ky = 0;
        kx = -1;
        if (x + kx >= 0 && fieldForServer[y + ky][x + kx][INDEX_COLOR] == chooseColor) {
            fieldForServer[y + ky][x + kx][INDEX_COLOR] = -1;
            countChange++;
            blastedBlocks.add(fieldForServer[y + ky][x + kx][INDEX_NAME]);
        }

        kx = 1;
        if (x + kx <= fieldForServer[y].length - 1 && fieldForServer[y + ky][x + kx][INDEX_COLOR] == chooseColor) {
            fieldForServer[y + ky][x + kx][INDEX_COLOR] = -1;
            countChange++;
            blastedBlocks.add(fieldForServer[y + ky][x + kx][INDEX_NAME]);
        }
        return countChange;
    }

    private void moveDestroyedCellsToTop(int[][][] fieldForServer) {

        for (int i = 0; i < fieldForServer[0].length; i++) {
            for (int j = 0; j < fieldForServer.length; j++) {
                if (fieldForServer[j][i][INDEX_COLOR] < 0) {
                    moveColumns(fieldForServer, i);
                    break;
                }
            }
        }
    }

    private void moveColumns(int[][][] fieldForServer, int x) {

        for (int i = fieldForServer.length - 1; i >= 0; i--) {
            for (int j = fieldForServer.length - 1; j > (fieldForServer.length - 1 - i); j--) {
                if (fieldForServer[j][x][INDEX_COLOR] < 0 && fieldForServer[j - 1][x][INDEX_COLOR] >= 0) {
                    int tempColor = fieldForServer[j][x][INDEX_COLOR];
                    fieldForServer[j][x][INDEX_COLOR] = fieldForServer[j - 1][x][INDEX_COLOR];
                    fieldForServer[j - 1][x][INDEX_COLOR] = tempColor;

                    int tempName = fieldForServer[j][x][INDEX_NAME];
                    fieldForServer[j][x][INDEX_NAME] = fieldForServer[j - 1][x][INDEX_NAME];
                    fieldForServer[j - 1][x][INDEX_NAME] = tempName;
                }
            }
        }
    }

    private void fillingEmptyCells(Player player) {
        int[][][] fieldForServer = player.getFieldForServer();

        for (int i = 0; i < fieldForServer.length; i++) {
            for (int j = 0; j < fieldForServer[i].length; j++) {
                if (fieldForServer[i][j][INDEX_COLOR] < 0) {

                    int indexColor = getRandomColor();
                    int nameBlock = player.getNextNameBlock();
                    fieldForServer[i][j][INDEX_COLOR] = indexColor;
                    fieldForServer[i][j][INDEX_NAME] = nameBlock;

                    player.getSpawnedBlocks().add(fieldForServer[i][j]);
                }
            }
        }
    }

    //TODO переделать на мапу
    public void spellMove(GameRoom gameRoom, String nameSpell) {
        Player player = gameRoom.getPlayer();
        Map<String, Spell> availableSpells = player.getAvailableSpellsForServer();

        Spell spell = availableSpells.get(nameSpell);
        if (spell != null) {
            int costSpell = spell.getCost();
            int mana = player.getMana();
            if (mana >= costSpell) {
                spell.activate();
            }
        } else {
            log.info("нет доступных заклинаний");
        }

//        int numSpell = -1;
//        for (int i = 0; i < availableSpells.size(); i++) {
//            if (availableSpells.get(i).getNameSpell().equals(nameSpell)) {
//                numSpell = i;
//                break;
//            }
//        }
//        if (numSpell < availableSpells.size() && numSpell >= 0) {
//            Spell chooseSpell = availableSpells.get(numSpell);
//            int costSpell = chooseSpell.getCost();
//            int mana = player.getMana();
//            if (mana >= costSpell) {
//                chooseSpell.activate();
//            }
//        } else {
//            log.info("нет доступных заклинаний");
//        }
    }

    //TODO удалять комнату при game over
    public boolean checkGameOver(SocketClient socketClient, GameRoom gameRoom) {
        boolean isGameOver = false;

        GameState gameState = gameRoom.getAdventureData().getGameState();

        if ((gameRoom.getPlayer().getHp() <= 0) ||
                (gameState == GameState.RESUME && !socketClient.getSocketChannel().isConnected())) {
            gameRoom.getAdventureData().setGameState(GameState.LOSE);
            isGameOver = true;
        } else if (gameRoom.getMonster().getHp() <= 0) {
            gameRoom.getAdventureData().setGameState(GameState.WIN);
            isGameOver = true;
        }
        return isGameOver;
    }
}
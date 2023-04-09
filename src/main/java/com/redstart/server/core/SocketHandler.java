package com.redstart.server.core;

import com.redstart.server.core.gamemechanics.GameRoomExecutor;
import com.redstart.server.core.message.JsonMessageConverter;
import com.redstart.server.core.message.SocketMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SocketHandler implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(SocketHandler.class);

    private final ServerSocketChannel serverSocketChannel;

    private final Map<SocketChannel, SocketClient> channels;

    private final GameRoomExecutor gameRoomExecutor;

    //private final GameLogicExecutor gameLogicExecutor;

    private final JsonMessageConverter jsonMessageConverter;

    private final SocketMessageHandler socketMessageHandler;

    private Selector selector;

    public SocketHandler(GameRoomExecutor gameRoomExecutor,
                         //GameLogicExecutor gameLogicExecutor,
                         ServerSocketChannel serverSocketChannel,
                         JsonMessageConverter jsonMessageConverter,
                         SocketMessageHandler socketMessageHandler) {

        this.gameRoomExecutor = gameRoomExecutor;
        this.socketMessageHandler = socketMessageHandler;
        gameRoomExecutor.setSocketHandler(this);

        //this.gameLogicExecutor = gameLogicExecutor;
        //gameLogicExecutor.setSocketHandler(this);

        this.serverSocketChannel = serverSocketChannel;
        this.jsonMessageConverter = jsonMessageConverter;

        channels = new ConcurrentHashMap<>();
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            selector = Selector.open();

            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            log.info("SocketHandler is started");

            while (true) {
                int readyChannels = selector.selectNow();
                if (readyChannels == 0) continue;

                for (SelectionKey key : selector.selectedKeys()) {
                    if (key.isValid()) {
                        SocketChannel socketChannel = null;
                        try {
                            if (key.isAcceptable()) {
                                acceptChannel();
                            }

                            if (key.isReadable()) {
                                socketChannel = (SocketChannel) key.channel();
                                readChannel(socketChannel);
                            }

                            if (key.isWritable()) {
                                socketChannel = (SocketChannel) key.channel();
                                writeChannel(socketChannel);
                            }
                        } catch (CancelledKeyException e) {
                            log.info("Client is disconnect");
                            channels.remove(socketChannel);
                            //gameRoomExecutor.removeGameRoom(socketChannel);
                            log.info("Count connection object in map - " + channels.size());
                        } catch (IOException e) {
                            log.info("Connection close " + socketChannel);
                            try {
                                socketChannel.close();
                                channels.remove(socketChannel);
                                //gameRoomExecutor.removeGameRoom(socketChannel);
                                log.info("Count connection object in map - " + channels.size());
                            } catch (IOException ex) {
                                log.warn("Connection close error ", ex);
                            }
                        }
                    }
                }
                selector.selectedKeys().clear();
            }


        } catch (IOException e) {
            log.error("Error open selector", e);
        }
    }

    private void acceptChannel() {
        try {
            SocketChannel socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);

            SocketClient socketClient = new SocketClient(socketChannel, this, selector, jsonMessageConverter);

            channels.put(socketChannel, socketClient);
            socketChannel.register(selector, SelectionKey.OP_READ);

            log.info("Connected " + socketChannel + ", count client Online - " + channels.size());

            //gameRoomExecutor.createGameRoom(socketClient);
        } catch (IOException e) {
            log.info("Can not connect client");
        }
    }

    private void readChannel(SocketChannel socketChannel) throws IOException {
        SocketClient socketClient = channels.get(socketChannel);
        ByteBuffer readBuffer = socketClient.getReadBuffer();

        int bytesRead = socketChannel.read(readBuffer);
//        if (bytesRead != 0) {
//            log.info("Прочитали байт " + bytesRead);
//        }

        if (bytesRead == -1) {
            log.info("Connection close " + socketChannel);
            throw new IOException();
        } else if ((bytesRead > 0 && readBuffer.get(readBuffer.position() - 1) == '\n')) {
            readBuffer.flip();
            //byte[] bytes = new byte[readBuffer.remaining()];
            //readBuffer.get(bytes);      //получаем массив байтов

            //TODO избавиться от постоянного создания стринг
            String clientMessage = new String(readBuffer.array(), readBuffer.position(), readBuffer.limit());

            log.info("message from client - " + clientMessage.replaceAll("[\\n\\r]", ""));
            //gameLogicExecutor.addTasksToExecute(socketClient, clientMessage);

            socketMessageHandler.handle(clientMessage, socketClient);

            readBuffer.clear();
        }
    }

    private void writeChannel(SocketChannel socketChannel) throws IOException {
        SocketClient socketClient;
        if ((socketClient = channels.get(socketChannel)) == null) {
            return;
        }
        ByteBuffer writeBuffer = socketClient.getWriteBuffer();

        if (writeBuffer.position() == 0) {
            byte[] bytesToWrite = socketClient.getWriteToSocketQueue().poll();
            if (bytesToWrite != null) {
                //log.info("положили в буфер - " + new String(bytesToWrite));
                writeBuffer.put(ByteBuffer.wrap(bytesToWrite));
                if (bytesToWrite.length != 0) {
                    if ((bytesToWrite[bytesToWrite.length - 1]) != '\n') {
                        writeBuffer.put((byte) '\n');
                    }
                }
            }
            writeBuffer.flip();
        }

        socketChannel.register(selector, SelectionKey.OP_READ);
        //log.info("Зарегистрировали на чтение");
        socketChannel.write(writeBuffer);

        if (!writeBuffer.hasRemaining()) {
            //log.info("отправили");
            writeBuffer.compact();
            writeBuffer.clear();
            Queue<byte[]> writeToSocketQueue = socketClient.getWriteToSocketQueue();
            if (!writeToSocketQueue.isEmpty()) {
                socketChannel.register(selector, SelectionKey.OP_WRITE);
                //log.info("Зарегистрировали на запись");
            }
        }

    }

//    private void writeToBuffer(SocketClient socketClient, byte[] bytesToWrite) {
//        SocketChannel socketChannel = socketClient.getSocketChannel();
//        if (channels.get(socketChannel) == null) {
//            return;
//        }
//        socketClient.addToWriteQueue(bytesToWrite);
//        //TODO проверить, если 10 раз положить в очередь и один раз вызвать read, то сколько считает
//        try {
//            socketChannel.register(selector, SelectionKey.OP_WRITE);
//        } catch (ClosedChannelException e) {
//            log.info("Connection is close");
//        }
//    }

//    public void addToWriteObject(SocketClient socketClient, GameRoom gameRoom) {
//        byte[] sendMessage = jsonMessageConverter.objectToJson(gameRoom.getAdventureData());
//        gameRoom.getPlayer().getBlastedBlocks().clear();
//        gameRoom.getPlayer().getSpawnedBlocks().clear();
//        writeToBuffer(socketClient, sendMessage);
//    }

//    public void addToWriteObject(SocketClient socketClient, SocketMessage socketMessage) {
//        byte[] sendMessage = jsonMessageConverter.objectToJson(socketMessage);
//        writeToBuffer(socketClient, sendMessage);
//    }

    public Map<SocketChannel, SocketClient> getChannels() {
        return channels;
    }
}

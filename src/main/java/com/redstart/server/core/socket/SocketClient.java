package com.redstart.server.core.socket;

import com.redstart.server.core.socket.message.JsonMessageConverter;
import com.redstart.server.core.socket.message.SocketMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class SocketClient {
    private static final Logger log = LoggerFactory.getLogger(SocketClient.class);

    private static final int READ_BUFFER_CAPACITY = 8000;

    private static final int WRITE_BUFFER_CAPACITY = 8000;

    private final SocketChannel socketChannel;

    private final ByteBuffer writeBuffer;

    private final ByteBuffer readBuffer;

    private final Queue<byte[]> writeToSocketQueue;

    private String login;

    private final SocketHandler socketHandler;
    private final Selector selector;
    private final JsonMessageConverter jsonMessageConverter;

    public SocketClient(SocketChannel socketChannel,
                        SocketHandler socketHandler,
                        Selector selector,
                        JsonMessageConverter jsonMessageConverter) {
        this.socketChannel = socketChannel;
        this.writeBuffer = ByteBuffer.allocate(WRITE_BUFFER_CAPACITY);
        this.readBuffer = ByteBuffer.allocate(READ_BUFFER_CAPACITY);
        this.writeToSocketQueue = new LinkedBlockingQueue<>();
        this.socketHandler = socketHandler;
        this.selector = selector;
        this.jsonMessageConverter = jsonMessageConverter;
    }

    public void addToWriteObject(SocketMessage socketMessage) {
        byte[] sendMessage = jsonMessageConverter.objectToJson(socketMessage);
        //log.info(new String(sendMessage));
        writeToBuffer(sendMessage);
    }

    private void writeToBuffer(byte[] bytesToWrite) {
        if (socketHandler.getChannels().get(socketChannel) == null) {
            return;
        }
        addToWriteQueue(bytesToWrite);
        //TODO проверить, если 10 раз положить в очередь и один раз вызвать read, то сколько считает
        try {
            socketChannel.register(selector, SelectionKey.OP_WRITE);
        } catch (ClosedChannelException e) {
            log.info("Connection is close");
        }
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    public ByteBuffer getWriteBuffer() {
        return writeBuffer;
    }

    public ByteBuffer getReadBuffer() {
        return readBuffer;
    }

    public Queue<byte[]> getWriteToSocketQueue() {
        return writeToSocketQueue;
    }

    public void addToWriteQueue(byte[] bytes) {
        writeToSocketQueue.add(bytes);
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}

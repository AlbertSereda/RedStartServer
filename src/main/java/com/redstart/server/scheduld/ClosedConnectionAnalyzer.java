package com.redstart.server.scheduld;

import com.redstart.server.core.socket.SocketClient;
import com.redstart.server.core.socket.SocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Map;

@Component
public class ClosedConnectionAnalyzer {
    private static final Logger log = LoggerFactory.getLogger(ClosedConnectionAnalyzer.class);
    private final Map<SocketChannel, SocketClient> channels;

    public ClosedConnectionAnalyzer(SocketHandler socketHandler) {
        channels = socketHandler.getChannels();
    }

    @Scheduled(cron = "0 0/30 * * * ?")
    public void analyze() {
        log.info("ClosedConnectionAnalyzer starting. There are {} connections on map", channels.size());
        Iterator<Map.Entry<SocketChannel, SocketClient>> iterator = channels.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<SocketChannel, SocketClient> entry = iterator.next();
            SocketChannel channel = entry.getKey();
            if (!channel.isConnected()) {
                log.info("Channel is disconnect. Will remove : {}", channel);
                iterator.remove();
            }
            if (!channel.isOpen()) {
                log.info("Channel is closed. Will remove : {}", channel);
                iterator.remove();
            }
        }
        log.info("ClosedConnectionAnalyzer finished");
    }
}

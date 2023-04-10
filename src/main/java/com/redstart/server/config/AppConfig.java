package com.redstart.server.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.redstart.server.core.socket.SocketClient;
import com.redstart.server.core.gamemechanics.GameRoom;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class AppConfig {

    @Bean
    public ServerSocketChannel serverSocketChannel(@Value("${adventure.port}") int port) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(port));
        serverSocketChannel.configureBlocking(false);
        return serverSocketChannel;
    }

    @Bean
    public ObjectMapper objectMapper() {
        //return new ObjectMapper();
        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .registerModule(new Jdk8Module());
        //.registerModule(new JavaTimeModule());
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper;
    }

    @Bean("socketMessageHandlerExecutor")
    public ExecutorService socketMessageHandlerExecutor(@Value("${socketMessageHandler.countThread}") int nThreads) {
        return Executors.newFixedThreadPool(nThreads);
    }

    @Bean("gameRoomRepositoryMap")
    public Map<SocketClient, GameRoom> gameRoomRepositoryMap() {
        return new ConcurrentHashMap<>();
    }
}

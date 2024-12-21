package com.elsa.learning.realtime.service;

import com.elsa.learning.realtime.model.Leaderboard;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import java.util.List;

@ApplicationScoped
public class LeaderboardConsumer {
    @Inject
    ObjectMapper objectMapper;

    @Inject
    WebSocketService webSocketService;

    // Consume the leaderboard updates from Kafka
    @Incoming("leaderboards")
    public void consumeLeaderboardUpdate(String leaderboardListMessage) throws JsonProcessingException {

        List<Leaderboard> leaderboards = objectMapper.readValue(leaderboardListMessage, new TypeReference<List<Leaderboard>>() {});
        if(!leaderboards.isEmpty()) {
            // Broadcast the updated leaderboard to the users connected to this room
            webSocketService.broadcastToRoom(leaderboards.get(0).getRoomUuid(), leaderboardListMessage);
        }
    }
}
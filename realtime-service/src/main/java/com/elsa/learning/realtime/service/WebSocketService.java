package com.elsa.learning.realtime.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/realtime/{roomUuid}/{userUuid}")
@ApplicationScoped
public class WebSocketService {

    private final Map<String, Session> userSessions = new ConcurrentHashMap<>();
    private final Map<String, String> userRoomMap = new ConcurrentHashMap<>(); // Maps user UUID to room UUID

    // Handle new WebSocket connection
    @OnOpen
    public void onOpen(Session session, @PathParam("userUuid") String userUuid, @PathParam("roomUuid") String roomUuid) {
        userSessions.put(userUuid, session);
        userRoomMap.put(userUuid, roomUuid);
        System.out.println("User " + userUuid + " connected to room " + roomUuid);
    }

    // Handle message received from WebSocket client
    @OnMessage
    public void onMessage(Session session, String message) {
        System.out.println("Received message from user: " + message);
    }

    // Handle WebSocket connection closure
    @OnClose
    public void onClose(Session session, @PathParam("userUuid") String userUuid) {
        userSessions.remove(userUuid);
        userRoomMap.remove(userUuid);
        System.out.println("User " + userUuid + " disconnected");
    }

    // Send message to all users connected to the room
    public void broadcastToRoom(String roomUuid, String message) {
        userSessions.forEach((userUuid, session) -> {
            String userRoomUuid = userRoomMap.get(userUuid);
            if (userRoomUuid != null && userRoomUuid.equals(roomUuid)) {
                try {
                    session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

package com.elsa.learning.room.service;

import com.elsa.learning.room.config.RoomQuestionServiceClient;
import com.elsa.learning.room.converter.RoomConverter;
import com.elsa.learning.room.entity.RoomEntity;
import com.elsa.learning.room.model.RoomResponse;
import com.elsa.learning.room.repository.RoomRepository;
import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import io.quarkus.redis.datasource.hash.ReactiveHashCommands;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoomServiceTest {

    @Mock
    ReactiveRedisDataSource reactiveRedisDataSource;

    @Mock
    ReactiveHashCommands<String, String, String> redisClient;

    @Mock
    RoomRepository roomRepository;

    @Mock
    @RestClient
    RoomQuestionServiceClient roomQuestionServiceClient;

    @InjectMocks
    RoomService roomService;

    @BeforeEach
    public void setUp() {
        when(reactiveRedisDataSource.hash(String.class, String.class, String.class)).thenReturn(redisClient);
        roomService.init();
    }

    @Test
    public void testCreateRoom() {
        String roomName = "Test Room";
        UUID uuid = UUID.randomUUID();
        RoomEntity roomEntity = new RoomEntity();
        roomEntity.setUuid(uuid.toString());
        roomEntity.setName(roomName);

        when(roomRepository.persist(any(RoomEntity.class))).thenReturn(Uni.createFrom().item(roomEntity));
        when(roomQuestionServiceClient.assignQuestionsToRoom(anyString())).thenReturn(Uni.createFrom().item(new ArrayList<>()));
        when(redisClient.hset(anyString(), anyMap())).thenReturn(Uni.createFrom().item(1L));

        Uni<RoomResponse> result = roomService.createRoom(roomName);

        RoomResponse roomResponse = result.await().indefinitely();
        assertNotNull(roomResponse);
        assertEquals(roomName, roomResponse.getName());
        assertEquals(uuid.toString(), roomResponse.getUuid());

        verify(roomRepository).persist(any(RoomEntity.class));
        verify(roomQuestionServiceClient).assignQuestionsToRoom(anyString());
        verify(redisClient).hset(anyString(), anyMap());
    }

    @Test
    public void testGetRoomFromRedis() {
        String uuid = UUID.randomUUID().toString();
        Map<String, String> roomMap = Map.of("uuid", uuid, "name", "Test Room");

        when(redisClient.hgetall(uuid)).thenReturn(Uni.createFrom().item(roomMap));

        Uni<Optional<RoomResponse>> result = roomService.getRoom(uuid);

        Optional<RoomResponse> roomResponse = result.await().indefinitely();
        assertTrue(roomResponse.isPresent());
        assertEquals("Test Room", roomResponse.get().getName());
        assertEquals(uuid, roomResponse.get().getUuid());

        verify(redisClient).hgetall(uuid);
    }

    @Test
    public void testGetRoomFromDatabase() {
        String uuid = UUID.randomUUID().toString();
        RoomEntity roomEntity = new RoomEntity();
        roomEntity.setUuid(uuid);
        roomEntity.setName("Test Room");

        when(redisClient.hgetall(uuid)).thenReturn(Uni.createFrom().item(Map.of()));
        when(roomRepository.findByUuid(uuid)).thenReturn(Uni.createFrom().item(roomEntity));
        when(redisClient.hset(anyString(), anyMap())).thenReturn(Uni.createFrom().item(1L));

        Uni<Optional<RoomResponse>> result = roomService.getRoom(uuid);

        Optional<RoomResponse> roomResponse = result.await().indefinitely();
        assertTrue(roomResponse.isPresent());
        assertEquals("Test Room", roomResponse.get().getName());
        assertEquals(uuid, roomResponse.get().getUuid());

        verify(redisClient).hgetall(uuid);
        verify(roomRepository).findByUuid(uuid);
        verify(redisClient).hset(anyString(), anyMap());
    }

    @Test
    public void testGetRoomNotFound() {
        String uuid = UUID.randomUUID().toString();

        when(redisClient.hgetall(uuid)).thenReturn(Uni.createFrom().item(new HashMap<>()));
        when(roomRepository.findByUuid(uuid)).thenReturn(Uni.createFrom().item(() -> null));

        Uni<Optional<RoomResponse>> result = roomService.getRoom(uuid);

        Optional<RoomResponse> roomResponse = result.await().indefinitely();
        assertFalse(roomResponse.isPresent());

        verify(redisClient).hgetall(uuid);
        verify(roomRepository).findByUuid(uuid);
    }
}
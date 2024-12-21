package com.elsa.learning.room.service;

import com.elsa.learning.room.config.RoomQuestionServiceClient;
import com.elsa.learning.room.converter.RoomConverter;
import com.elsa.learning.room.entity.RoomEntity;
import com.elsa.learning.room.model.RoomResponse;
import com.elsa.learning.room.repository.RoomRepository;
import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import io.quarkus.redis.datasource.hash.ReactiveHashCommands;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class RoomService {
    @Inject
    ReactiveRedisDataSource reactiveRedisDataSource;

    private ReactiveHashCommands<String, String, String> redisClient;

    @Inject
    RoomRepository roomRepository;

    @Inject
    @RestClient
    RoomQuestionServiceClient roomQuestionServiceClient;

    @Inject
    public void init() {
        this.redisClient = reactiveRedisDataSource.hash(String.class, String.class, String.class);
    }

    public Uni<RoomResponse> createRoom(String name) {
        UUID uuid = UUID.randomUUID();

        RoomEntity room = new RoomEntity();
        room.setUuid(uuid.toString());
        room.setName(name);

        return roomRepository
                .persist(room)
                .onItem().transformToUni(roomEntity -> roomQuestionServiceClient.assignQuestionsToRoom(uuid.toString())
                        .onItem().transformToUni(questions -> {
                            // Cache the room entity in Redis
                            Map<String, String> roomMap = Map.of(
                                    "uuid", roomEntity.getUuid().toString(),
                                    "name", roomEntity.getName()
                            );
                            return redisClient.hset(uuid.toString(), roomMap)
                                    .replaceWith(RoomConverter.toResponse(roomEntity));
                        }));
    }

    public Uni<Optional<RoomResponse>> getRoom(String uuid) {
        return redisClient.hgetall(uuid)
                .onItem().transformToUni(roomMap -> {
                    if (roomMap.isEmpty()) {
                        // If not found in Redis, fetch from the database
                        return roomRepository.findByUuid(uuid)
                                .onItem().transformToUni(roomEntity -> {
                                            if (roomEntity != null) {
                                                // Cache the room entity in Redis
                                                Map<String, String> tempRoomMap = Map.of(
                                                        "uuid", roomEntity.getUuid().toString(),
                                                        "name", roomEntity.getName()
                                                );
                                                return redisClient.hset(uuid, tempRoomMap)
                                                        .replaceWith(Optional.of(RoomConverter.toResponse(roomEntity)));
                                            } else {
                                                return Uni.createFrom().item(Optional.empty());
                                            }
                                        }
                                );
                    } else {
                        // If found in Redis, return the cached room
                        RoomResponse roomResponse = new RoomResponse();
                        roomResponse.setUuid(roomMap.get("uuid"));
                        roomResponse.setName(roomMap.get("name"));
                        return Uni.createFrom().item(Optional.of(roomResponse));
                    }
                });
    }
}
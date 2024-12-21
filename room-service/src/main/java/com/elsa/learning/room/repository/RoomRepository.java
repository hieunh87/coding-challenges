package com.elsa.learning.room.repository;

import com.elsa.learning.room.entity.RoomEntity;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RoomRepository implements PanacheRepository<RoomEntity> {
    public Uni<RoomEntity> findByUuid(String uuid) {
        return find("uuid", uuid).firstResult();
    }
}

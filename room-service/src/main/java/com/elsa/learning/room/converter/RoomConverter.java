package com.elsa.learning.room.converter;

import com.elsa.learning.room.entity.RoomEntity;
import com.elsa.learning.room.model.RoomResponse;

public class RoomConverter {
    public static RoomResponse toResponse(RoomEntity entity) {
        RoomResponse response = new RoomResponse();
        response.setUuid(entity.getUuid().toString());
        response.setName(entity.getName());
        return response;
    }
}

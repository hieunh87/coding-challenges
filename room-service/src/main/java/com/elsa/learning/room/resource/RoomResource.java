package com.elsa.learning.room.resource;


import com.elsa.learning.room.model.RoomRequest;
import com.elsa.learning.room.service.RoomService;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    @Inject
    RoomService roomService;

    @POST
    @WithTransaction
    public Uni<Response> createRoom(RoomRequest roomRequest) {
        if (roomRequest.getName() == null || roomRequest.getName().trim().isEmpty()) {
            return Uni.createFrom().item(Response.status(Response.Status.BAD_REQUEST)
                    .entity("Room name cannot be empty").build());
        }

        return roomService.createRoom(roomRequest.getName())
                .onItem().transform(room -> Response.status(Response.Status.CREATED)
                        .entity(room)
                        .build())
                .onFailure().recoverWithItem(throwable -> Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("Failed to create room: " + throwable.getMessage())
                        .build());
    }

    @GET
    @Path("/{uuid}")
    @WithTransaction
    public Uni<Response> getRoom(@PathParam("uuid") String uuid) {
        return roomService.getRoom(uuid)
                .onItem().transform(roomOpt -> roomOpt
                        .map(room -> Response.ok(room).build())
                        .orElseGet(() -> Response.status(Response.Status.NOT_FOUND)
                                .entity("Room not found").build())
                );
    }
}
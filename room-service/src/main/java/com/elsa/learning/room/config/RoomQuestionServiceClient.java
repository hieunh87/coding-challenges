package com.elsa.learning.room.config;

import com.elsa.learning.room.model.QuestionResponse;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@Path("/room-question")
@RegisterRestClient(configKey = "room-question-service")
@Produces(MediaType.APPLICATION_JSON)
public interface RoomQuestionServiceClient {

    @POST
    @Path("/{roomUuid}")
    Uni<List<QuestionResponse>> assignQuestionsToRoom(@PathParam("roomUuid") String roomUuid);
}

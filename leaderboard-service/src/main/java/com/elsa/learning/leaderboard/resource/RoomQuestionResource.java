package com.elsa.learning.leaderboard.resource;

import com.elsa.learning.leaderboard.model.QuestionResponse;
import com.elsa.learning.leaderboard.model.QuestionWithoutAnswerResponse;
import com.elsa.learning.leaderboard.service.RoomCreationService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;

import java.util.List;

@Path("/room-question")
@Produces("application/json")
@Consumes("application/json")
public class RoomQuestionResource {
    @Inject
    RoomCreationService roomCreationService;

    @Path("/{roomUuid}")
    @POST
    @Transactional
    public List<QuestionResponse> assignQuestionsToRoom(@PathParam("roomUuid") String roomUuid) {
        return roomCreationService.fetchQuestionsForRoom(roomUuid);
    }

    @Path("/{roomUuid}")
    @GET
    @Transactional
    public List<QuestionWithoutAnswerResponse> getQuestionsByRoomUuid(@PathParam("roomUuid") String roomUuid) {
        return roomCreationService.getQuestionsByRoomUuid(roomUuid);
    }
}

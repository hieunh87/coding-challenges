package com.elsa.learning.answer.resource;

import com.elsa.learning.answer.model.AnswerListRequest;
import com.elsa.learning.answer.service.AnswerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

@Path("/answers")
@Produces("application/json")
@Consumes("application/json")
public class AnswerResource {

    @Inject
    AnswerService answerService;

    @POST
    public Uni<Response> submitAnswer(AnswerListRequest answerListRequest) throws JsonProcessingException {
        return answerService.submitAnswer(answerListRequest).onItem().transform(item -> Response.ok().build());
    }
}

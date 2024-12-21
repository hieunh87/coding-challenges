package com.elsa.learning.question.resource;

import com.elsa.learning.question.entity.QuestionEntity;
import com.elsa.learning.question.service.QuestionService;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/questions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class QuestionResource {

    @Inject
    QuestionService questionService;

    @GET
    @Path("/random-by-level")
    @WithTransaction
    public Uni<List<QuestionEntity>> getRandomQuestionsByLevel(@QueryParam("level") int level) {
        return questionService.getRandomQuestionsByLevel(level, 5);
    }
}
package com.elsa.learning.leaderboard.config;

import com.elsa.learning.leaderboard.model.QuestionResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@RegisterRestClient(configKey = "question-service")
@Path("/questions")
@Produces(MediaType.APPLICATION_JSON)
public interface QuestionServiceClient {

    @GET
    @Path("/random-by-level")
    List<QuestionResponse> getQuestionsByLevel(@QueryParam("level") int level);
}

package com.elsa.learning.answer.service;

import com.elsa.learning.answer.model.AnswerListRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@ApplicationScoped
public class AnswerService {
    @Inject
    @Channel("answers")
    Emitter<String> answerRequestEmitter;

    @Inject
    ObjectMapper objectMapper;

    public Uni<Void> submitAnswer(AnswerListRequest answerListRequest) throws JsonProcessingException {
        String message = objectMapper.writeValueAsString(answerListRequest);
        answerRequestEmitter.send(message);

        // Return void, indicating successful handling
        return Uni.createFrom().voidItem();
    }
}

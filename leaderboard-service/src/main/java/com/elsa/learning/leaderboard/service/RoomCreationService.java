package com.elsa.learning.leaderboard.service;

import com.elsa.learning.leaderboard.config.QuestionServiceClient;
import com.elsa.learning.leaderboard.entity.RoomQuestion;
import com.elsa.learning.leaderboard.model.QuestionResponse;
import com.elsa.learning.leaderboard.model.QuestionWithoutAnswerResponse;
import com.elsa.learning.leaderboard.repository.RoomQuestionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class RoomCreationService {
    @Inject
    @RestClient
    QuestionServiceClient questionServiceClient;

    @Inject
    RoomQuestionRepository roomQuestionRepository;

    public List<QuestionResponse> fetchQuestionsForRoom(String roomUuid) {
        return questionServiceClient.getQuestionsByLevel(1).stream()
                .map(question -> {
                    saveRoomQuestion(roomUuid, question);
                    return question;
                }).collect(Collectors.toList());

    }

    public List<QuestionWithoutAnswerResponse> getQuestionsByRoomUuid(String roomUuid) {
        return roomQuestionRepository.find("roomUuid", UUID.fromString(roomUuid)).list().stream().map(roomQuestion -> {
            {
                QuestionWithoutAnswerResponse question = new QuestionWithoutAnswerResponse();
                question.setUuid(roomQuestion.getQuestionUuid().toString());
                question.setLevel(roomQuestion.getLevel());
                question.setText(roomQuestion.getText());
                question.setOptions(roomQuestion.getOptions());
                return question;
            }
        }).collect(Collectors.toList());
    }

    private void saveRoomQuestion(String roomUuid, QuestionResponse question) {
        RoomQuestion roomQuestion = new RoomQuestion();
        roomQuestion.setRoomUuid(UUID.fromString(roomUuid));
        roomQuestion.setQuestionUuid(UUID.fromString(question.getUuid()));
        roomQuestion.setLevel(question.getLevel());
        roomQuestion.setText(question.getText());
        roomQuestion.setOptions(question.getOptions());
        roomQuestion.setCorrectOption(question.getCorrectOption());
        roomQuestionRepository.persist(roomQuestion);
    }
}

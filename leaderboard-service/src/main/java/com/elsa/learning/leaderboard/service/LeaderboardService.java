package com.elsa.learning.leaderboard.service;

import com.elsa.learning.leaderboard.entity.Leaderboard;
import com.elsa.learning.leaderboard.entity.RoomQuestion;
import com.elsa.learning.leaderboard.entity.UserAnswer;
import com.elsa.learning.leaderboard.model.AnswerListRequest;
import com.elsa.learning.leaderboard.model.AnswerRequest;
import com.elsa.learning.leaderboard.repository.LeaderboardRepository;
import com.elsa.learning.leaderboard.repository.RoomQuestionRepository;
import com.elsa.learning.leaderboard.repository.UserAnswerRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class LeaderboardService {
    @Inject
    @Channel("leaderboards")
    Emitter<String> leaderboardEmitter;

    @Inject
    ObjectMapper objectMapper;

    @Inject
    UserAnswerRepository userAnswerRepository;

    @Inject
    RoomQuestionRepository roomQuestionRepository;

    @Inject
    LeaderboardRepository leaderboardRepository;

    @Incoming("answers")
    @Transactional
    public void consume(String answerListMessage) {
        AnswerListRequest answerListRequest = parseAnswerMessage(answerListMessage);
        int totalScore = 0;
        totalScore = answerListRequest.getAnswers().stream().map(answer -> {
            UserAnswer userAnswer = new UserAnswer();
            userAnswer.setUserUuid(UUID.fromString(answerListRequest.getUserUuid()));
            userAnswer.setQuestionUuid(UUID.fromString(answer.getQuestionUuid()));
            userAnswer.setAnswer(answer.getAnswerOption());
            userAnswerRepository.persist(userAnswer);

            return isAnswerCorrect(answer, answerListRequest.getRoomUuid()) ? 10 : 0;
        }).reduce(0, Integer::sum);

        Leaderboard leaderboard = new Leaderboard();
        leaderboard.setRoomUuid(UUID.fromString(answerListRequest.getRoomUuid()));
        leaderboard.setUserUuid(UUID.fromString(answerListRequest.getUserUuid()));
        leaderboard.setScore(totalScore);
        leaderboard.setUserName(answerListRequest.getUserName());
        leaderboardRepository.updateScore(leaderboard);

        List<Leaderboard> leaderboardList = leaderboardRepository.find("roomUuid", UUID.fromString(answerListRequest.getRoomUuid())).list();

        emitLeaderboardUpdate(leaderboardList);
    }

    private void emitLeaderboardUpdate(List<Leaderboard> leaderboards) {
        try {
            leaderboardEmitter.send(objectMapper.writeValueAsString(leaderboards));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isAnswerCorrect(AnswerRequest answer, String roomUuid) {
        RoomQuestion roomQuestion = roomQuestionRepository.find("roomUuid = ?1 and questionUuid = ?2", UUID.fromString(roomUuid), UUID.fromString(answer.getQuestionUuid()))
                .firstResult();
        if (roomQuestion != null && roomQuestion.getCorrectOption() == answer.getAnswerOption()) return true;
        else return false;
    }

    private AnswerListRequest parseAnswerMessage(String message) {
        try {
            return objectMapper.readValue(message, AnswerListRequest.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

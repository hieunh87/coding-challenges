package com.elsa.learning.question.service;

import com.elsa.learning.question.entity.QuestionEntity;
import com.elsa.learning.question.repository.QuestionRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class QuestionService {

    @Inject
    QuestionRepository questionRepository;

    public Uni<List<QuestionEntity>> getRandomQuestionsByLevel(int level, int count) {
        return questionRepository.findRandomQuestionsByLevel(level, count);
    }
}
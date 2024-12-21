package com.elsa.learning.question.repository;


import com.elsa.learning.question.entity.QuestionEntity;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class QuestionRepository implements PanacheRepository<QuestionEntity> {

    public Uni<QuestionEntity> findByUuid(UUID uuid) {
        return find("uuid", uuid).firstResult();
    }

    public Uni<List<QuestionEntity>> findRandomQuestionsByLevel(int level, int count) {
        return find("level = ?1 ORDER BY RANDOM()", level).page(0, count).list();
    }
}

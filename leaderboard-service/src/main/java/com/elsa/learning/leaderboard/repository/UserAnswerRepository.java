package com.elsa.learning.leaderboard.repository;

import com.elsa.learning.leaderboard.entity.UserAnswer;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserAnswerRepository implements PanacheRepository<UserAnswer> {
}

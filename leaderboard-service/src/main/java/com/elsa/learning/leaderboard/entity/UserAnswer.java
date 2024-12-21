package com.elsa.learning.leaderboard.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "user_answer")
public class UserAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_uuid", nullable = false)
    private UUID userUuid;

    @Column(name = "question_uuid", nullable = false)
    private UUID questionUuid;

    @Column(name = "answer", nullable = false)
    private int answer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(UUID userUuid) {
        this.userUuid = userUuid;
    }

    public UUID getQuestionUuid() {
        return questionUuid;
    }

    public void setQuestionUuid(UUID questionUuid) {
        this.questionUuid = questionUuid;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }
}

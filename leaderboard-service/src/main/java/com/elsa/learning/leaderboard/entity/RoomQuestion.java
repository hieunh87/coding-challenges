package com.elsa.learning.leaderboard.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "room_question")
public class RoomQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_uuid", nullable = false)
    private UUID roomUuid;

    @Column(name = "question_uuid", nullable = false)
    private UUID questionUuid;

    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    private String options;

    @Column(name = "correct_option", nullable = false)
    private int correctOption;

    @Column(name = "level", nullable = false)
    private int level;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getRoomUuid() {
        return roomUuid;
    }

    public void setRoomUuid(UUID roomUuid) {
        this.roomUuid = roomUuid;
    }

    public UUID getQuestionUuid() {
        return questionUuid;
    }

    public void setQuestionUuid(UUID questionUuid) {
        this.questionUuid = questionUuid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public int getCorrectOption() {
        return correctOption;
    }

    public void setCorrectOption(int correctOption) {
        this.correctOption = correctOption;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}

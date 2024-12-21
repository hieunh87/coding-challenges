package com.elsa.learning.leaderboard.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "leaderboard")
public class Leaderboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_uuid", nullable = false)
    private UUID roomUuid;

    @Column(name = "user_uuid", nullable = false)
    private UUID userUuid;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "score", nullable = false)
    private int score;

    // Getters and Setters
    public UUID getRoomUuid() {
        return roomUuid;
    }

    public void setRoomUuid(UUID roomUuid) {
        this.roomUuid = roomUuid;
    }

    public UUID getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(UUID userUuid) {
        this.userUuid = userUuid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}

package com.elsa.learning.realtime.model;

public class Leaderboard {
    private String userUuid;
    private String userName;
    private String roomUuid;
    private int score;

    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRoomUuid() {
        return roomUuid;
    }

    public void setRoomUuid(String roomUuid) {
        this.roomUuid = roomUuid;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
